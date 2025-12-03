package com.maxinhai.platform.service.alarm.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.alarm.AlarmImageQueryDTO;
import com.maxinhai.platform.mapper.alarm.AlarmImageMapper;
import com.maxinhai.platform.po.alarm.AlarmImage;
import com.maxinhai.platform.service.alarm.AlarmImageService;
import com.maxinhai.platform.utils.ImageBase64Utils;
import com.maxinhai.platform.vo.alarm.AlarmImageVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class AlarmImageServiceImpl extends ServiceImpl<AlarmImageMapper, AlarmImage> implements AlarmImageService {

    private final AlarmImageMapper alarmImageMapper;

    @Override
    public Page<AlarmImageVO> searchByPage(AlarmImageQueryDTO param) {
        return alarmImageMapper.selectJoinPage(param.getPage(), AlarmImageVO.class, new MPJLambdaWrapper<AlarmImage>()
                .eq(StrUtil.isNotBlank(param.getAlarmId()), AlarmImage::getAlarmId, param.getAlarmId())
                .like(StrUtil.isNotBlank(param.getImageName()), AlarmImage::getImageName, param.getImageName())
                .orderByDesc(AlarmImage::getCreateTime));
    }

    @Override
    public AlarmImageVO getInfo(String id) {
        return alarmImageMapper.selectJoinOne(AlarmImageVO.class, new MPJLambdaWrapper<AlarmImage>()
                .eq(AlarmImage::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        alarmImageMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    @Async("ioIntensiveExecutor") // 指定异步线程池
    public void asyncHandleAlarmImages(String alarmId, List<String> imgs) {
        try {
            // 收集所有图片数据，准备批量插入
            List<AlarmImage> alarmImageList = new ArrayList<>(imgs.size());
            for (String imgStr : imgs) {
                AlarmImage alarmImage = new AlarmImage();
                alarmImage.setAlarmId(alarmId);
                String saveFileName = DateUtil.format(new Date(), "yyyyMMdd_HHmmss_SSS") + ".jpg";
                String savePath = ImageBase64Utils.base64ToImage(imgStr, saveFileName);
                alarmImage.setImageName(saveFileName);
                alarmImage.setImageUrl(savePath);
                alarmImageList.add(alarmImage);
            }

            // 批量插入数据库（减少数据库交互次数）
            if (!alarmImageList.isEmpty()) {
                alarmImageMapper.batchInsert(alarmImageList); // 需在Mapper中实现批量插入方法
            }
        } catch (Exception e) {
            // 异常处理：记录日志（必要时可加重试机制）
            log.error("异步处理告警图片失败，alarmId：{}，错误信息：{}", alarmId, e.getMessage());
            // 若需要重试，可使用Spring的Retry注解，或手动实现重试逻辑
        }
    }
}
