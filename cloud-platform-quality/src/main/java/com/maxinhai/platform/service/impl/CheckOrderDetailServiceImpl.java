package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.CheckOrderDetailAddDTO;
import com.maxinhai.platform.dto.CheckOrderDetailEditDTO;
import com.maxinhai.platform.dto.CheckOrderDetailQueryDTO;
import com.maxinhai.platform.enums.CheckStatus;
import com.maxinhai.platform.enums.ControlType;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.mapper.CheckOrderDetailMapper;
import com.maxinhai.platform.po.CheckOrderDetail;
import com.maxinhai.platform.service.CheckOrderDetailService;
import com.maxinhai.platform.vo.CheckOrderDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CheckOrderDetailServiceImpl extends ServiceImpl<CheckOrderDetailMapper, CheckOrderDetail> implements CheckOrderDetailService {

    @Resource
    private CheckOrderDetailMapper checkOrderDetailMapper;

    @Override
    public Page<CheckOrderDetailVO> searchByPage(CheckOrderDetailQueryDTO param) {
        Page<CheckOrderDetailVO> pageResult = checkOrderDetailMapper.selectJoinPage(param.getPage(), CheckOrderDetailVO.class,
                new MPJLambdaWrapper<CheckOrderDetail>()
                        .eq(StrUtil.isNotBlank(param.getCheckOrderId()), CheckOrderDetail::getCheckOrderId, param.getCheckOrderId())
                        .orderByDesc(CheckOrderDetail::getCreateTime));
        return pageResult;
    }

    @Override
    public CheckOrderDetailVO getInfo(String id) {
        return checkOrderDetailMapper.selectJoinOne(CheckOrderDetailVO.class,
                new MPJLambdaWrapper<CheckOrderDetail>()
                        .eq(StrUtil.isNotBlank(id), CheckOrderDetail::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        checkOrderDetailMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(CheckOrderDetailEditDTO param) {
        CheckOrderDetail user = BeanUtil.toBean(param, CheckOrderDetail.class);
        checkOrderDetailMapper.updateById(user);
    }

    @Override
    public void add(CheckOrderDetailAddDTO param) {
        CheckOrderDetail user = BeanUtil.toBean(param, CheckOrderDetail.class);
        checkOrderDetailMapper.insert(user);
    }

    @Override
    public List<CheckOrderDetailVO> getCheckItemList(String checkOrderId) {
        return checkOrderDetailMapper.selectJoinList(CheckOrderDetailVO.class, new MPJLambdaWrapper<CheckOrderDetail>()
                .select(CheckOrderDetail::getId, CheckOrderDetail::getItemCode, CheckOrderDetail::getItemName,
                        CheckOrderDetail::getControlType, CheckOrderDetail::getMinValue, CheckOrderDetail::getMaxValue)
                .eq(CheckOrderDetail::getCheckOrderId, checkOrderId)
                .orderByAsc(CheckOrderDetail::getCreateTime));
    }

    @Override
    public void filing(List<CheckOrderDetailEditDTO> itemList) {
        if (CollectionUtils.isEmpty(itemList)) {
            throw new BusinessException("检测项不存在！");
        }
        List<CheckOrderDetail> detailList = itemList.stream().map(item -> {
            CheckOrderDetail detail = new CheckOrderDetail();
            detail.setId(item.getId());
            switch (item.getControlType()) {
                case QL:
                    // 定性
                    handleQualitative(item);
                    break;
                case QT:
                    // 定量
                    handleQuantitative(item);
                    break;
                case MI:
                    // 手动输入
                    handleManualInput(item);
                    break;
                default:
            }
            detail.setCheckValue(item.getCheckValue());
            detail.setCheckResult(item.getCheckResult());
            detail.setStatus(CheckStatus.YES);
            return detail;
        }).collect(Collectors.toList());
        this.saveBatch(detailList);
    }

    /**
     * 处理定性检测项
     *
     * @param item
     */
    private void handleQualitative(CheckOrderDetailEditDTO item) {
        String checkResult = item.getCheckResult();
        if (StrUtil.isEmpty(checkResult)) {
            throw new BusinessException("定性检测结果不能为空：" + item.getItemName());
        }
    }

    /**
     * 处理定量检测项
     *
     * @param item
     */
    private void handleQuantitative(CheckOrderDetailEditDTO item) {
        BigDecimal minValue = item.getMinValue();
        BigDecimal maxValue = item.getMaxValue();
        BigDecimal checkValue = item.getCheckValue();

        // 1.校验必要参数
        if (checkValue == null) {
            throw new BusinessException("定量检测值不能为空：" + item.getItemName());
        }
        // 若min和max都为null，无判断依据
        if (minValue == null && maxValue == null) {
            throw new BusinessException("定量检测缺少参考范围：" + item.getItemName());
        }

        // 2.根据范围判断结果
        boolean isQualified;
        if (minValue != null && maxValue == null) {
            // 仅下限：checkValue >= minValue 为合格
            isQualified = checkValue.compareTo(minValue) >= 0;
        } else if (minValue == null && maxValue != null) {
            // 仅上限：checkValue <= maxValue 为合格
            isQualified = checkValue.compareTo(maxValue) <= 0;
        } else {
            // 区间范围：minValue <= checkValue <= maxValue 为合格
            isQualified = checkValue.compareTo(minValue) >= 0
                    && checkValue.compareTo(maxValue) <= 0;
        }

        item.setCheckResult(isQualified ? "合格" : "不合格");
    }

    /**
     * 处理手动输入检测项
     *
     * @param item
     */
    private void handleManualInput(CheckOrderDetailEditDTO item) {
        String checkResult = item.getCheckResult();
        if (StrUtil.isEmpty(checkResult)) {
            throw new BusinessException("手动输入检测结果不能为空：" + item.getItemName());
        }
    }
}
