package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.CodeRuleAddDTO;
import com.maxinhai.platform.dto.CodeRuleEditDTO;
import com.maxinhai.platform.dto.CodeRuleQueryDTO;
import com.maxinhai.platform.enums.ResetCycle;
import com.maxinhai.platform.enums.Status;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.mapper.CodeRuleMapper;
import com.maxinhai.platform.po.CodeRule;
import com.maxinhai.platform.service.CodeRuleService;
import com.maxinhai.platform.service.CommonCodeCheckService;
import com.maxinhai.platform.vo.CodeRuleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CodeRuleServiceImpl extends ServiceImpl<CodeRuleMapper, CodeRule> implements CodeRuleService {

    @Resource
    private CodeRuleMapper codeRuleMapper;
    @Resource
    private CommonCodeCheckService commonCodeCheckService;

    @Override
    public Page<CodeRuleVO> searchByPage(CodeRuleQueryDTO param) {
        return codeRuleMapper.selectJoinPage(param.getPage(), CodeRuleVO.class,
                new MPJLambdaWrapper<CodeRule>()
                        .like(StrUtil.isNotBlank(param.getRuleCode()), CodeRule::getRuleCode, param.getRuleCode())
                        .like(StrUtil.isNotBlank(param.getRuleName()), CodeRule::getRuleName, param.getRuleName())
                        .orderByDesc(CodeRule::getCreateTime));
    }

    @Override
    public CodeRuleVO getInfo(String id) {
        return codeRuleMapper.selectJoinOne(CodeRuleVO.class, new MPJLambdaWrapper<CodeRule>().eq(CodeRule::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        codeRuleMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(CodeRuleEditDTO param) {
        CodeRule user = BeanUtil.toBean(param, CodeRule.class);
        codeRuleMapper.updateById(user);
    }

    @Override
    public void add(CodeRuleAddDTO param) {
        boolean unique = commonCodeCheckService.isCodeUnique(CodeRule.class, CodeRule::getRuleCode, param.getRuleCode());
        if(!unique) {
            throw new BusinessException("编码规则【" + param.getRuleCode() + "】已存在！");
        }
        CodeRule user = BeanUtil.toBean(param, CodeRule.class);
        codeRuleMapper.insert(user);
    }

    @Override
    public List<String> generateCode(String ruleCode, int batchSize) {
        List<String> codeList = new ArrayList<>(batchSize);
        CodeRule codeRule = codeRuleMapper.selectOne(new LambdaQueryWrapper<CodeRule>()
                .select(CodeRule::getId, CodeRule::getRuleCode, CodeRule::getRuleName, CodeRule::getPrefix, CodeRule::getDateFlag, CodeRule::getDateFormat,
                        CodeRule::getCurrentSequence, CodeRule::getSequenceLength, CodeRule::getResetCycle, CodeRule::getLastResetTime)
                .eq(CodeRule::getRuleCode, ruleCode)
                .eq(CodeRule::getStatus, Status.Enable));
        if (Objects.isNull(codeRule)) {
            throw new BusinessException("编码规则【" + ruleCode + "】不存在或已禁用!");
        }

        if(!ResetCycle.NEVER.equals(codeRule.getResetCycle())) {
            // 使用Hutool的DateTime简化日期处理
            DateTime lastRestTime = DateUtil.date(codeRule.getLastResetTime());
            DateTime nextResetTime = calculateNextCycleResetTime(codeRule.getResetCycle(), lastRestTime);
            DateTime currentTime = DateUtil.date(new Date());
            // 当前时间处于上一次重置时间之前、下一次重置时间之后，当前序列号为0
            if(currentTime.isAfter(nextResetTime) && currentTime.isBefore(lastRestTime)) {
                codeRule.setCurrentSequence(0L);
            }
            // 当前时间是下一次重置时间，当前序列号重置为0，上次重置时间设置为当前时间
            if(currentTime.compareTo(nextResetTime) == 0) {
                codeRule.setCurrentSequence(0L);
                codeRule.setLastResetTime(currentTime);
            }
        }

        for (int i = 0; i < batchSize; i++) {
            long newCurrentSequence = codeRule.getCurrentSequence() + 1;
            // 前缀 + 日期 + 递增序列号
            String newCode = codeRule.getPrefix() +
                    (codeRule.getDateFlag() ? DateUtil.format(new Date(), codeRule.getDateFormat()) : "") +
                    padWithZeros(newCurrentSequence, codeRule.getSequenceLength());
            codeList.add(newCode);

            // 更新当前序列号
            codeRule.setCurrentSequence(newCurrentSequence);
        }
        codeRuleMapper.updateById(codeRule);
        return codeList;
    }

    /**
     * 对数字进行补零处理，返回指定长度的字符串
     *
     * @param num    要处理的数字
     * @param length 目标字符串长度（包含数字本身和补的零）
     * @return 补零后的字符串
     * @throws IllegalArgumentException 如果长度小于数字的实际位数或为负数
     */
    private static String padWithZeros(long num, int length) {
        // 边界校验
        if (length < 0) {
            throw new IllegalArgumentException("长度不能为负数: " + length);
        }

        // 计算数字的实际位数（优化性能，避免不必要的格式化）
        int numDigits = num == 0 ? 1 : (int) (Math.log10(num) + 1);

        // 检查是否超出目标长度
        if (numDigits > length) {
            throw new IllegalArgumentException("数字位数 (" + numDigits + ") 超过目标长度 (" + length + ")");
        }

        // 计算需要补的零数量
        int zerosToAdd = length - numDigits;

        // 高效构建结果（使用StringBuilder比String.format在高频调用时性能更优）
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < zerosToAdd; i++) {
            sb.append('0');
        }
        sb.append(num);

        return sb.toString();
    }

    /**
     * 计算下一个重置周期
     * @param resetCycle 重置周期
     * @param lastRestTime 上一个重置时间
     * @return
     */
    private static DateTime calculateNextCycleResetTime(ResetCycle resetCycle, Date lastRestTime) {
        DateTime dateTime = null;
        switch (resetCycle) {
            case DAY:
                dateTime = DateUtil.offsetDay(lastRestTime, 1);
                break;
            case WEEK:
                dateTime = DateUtil.offsetWeek(lastRestTime, 1);
                break;
            case MONTH:
                dateTime = DateUtil.offsetMonth(lastRestTime, 1);
                break;
            case QUARTER:
                dateTime = DateUtil.offsetMonth(lastRestTime, 3);
                break;
            case YEAR:
                dateTime = DateUtil.offsetYear(lastRestTime, 1);
                break;
            default:
                throw new IllegalArgumentException("不支持的周期类型: " + resetCycle);
        }
        return dateTime;
    }

}
