package com.maxinhai.platform.service.alarm.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.bo.AlgorithmAlarmBO;
import com.maxinhai.platform.dto.alarm.AlarmQueryDTO;
import com.maxinhai.platform.dto.alarm.RealTimeAlarmDTO;
import com.maxinhai.platform.enums.AlarmStatus;
import com.maxinhai.platform.handler.StringHandler;
import com.maxinhai.platform.mapper.alarm.AlarmImageMapper;
import com.maxinhai.platform.mapper.alarm.AlarmMapper;
import com.maxinhai.platform.mapper.alarm.AlgorithmMapper;
import com.maxinhai.platform.po.alarm.Alarm;
import com.maxinhai.platform.po.alarm.AlarmImage;
import com.maxinhai.platform.po.alarm.Algorithm;
import com.maxinhai.platform.service.alarm.AlarmService;
import com.maxinhai.platform.utils.ImageBase64Utils;
import com.maxinhai.platform.vo.alarm.AlarmVO;
import com.maxinhai.platform.vo.alarm.CountAlarmInfoVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class AlarmServiceImpl extends ServiceImpl<AlarmMapper, Alarm> implements AlarmService {

    private final AlgorithmMapper algorithmMapper;
    private final AlarmMapper alarmMapper;
    private final AlarmImageMapper alarmImageMapper;
    private final StringHandler stringHandler;
    // 算法缓存
    private static final Map<String, Algorithm> algorithmMap = new ConcurrentHashMap<>();
    private static final Map<String, Algorithm> idAlgorithmMap = new ConcurrentHashMap<>();

    @Override
    public Page<AlarmVO> searchByPage(AlarmQueryDTO param) {
        return alarmMapper.selectJoinPage(param.getPage(), AlarmVO.class, new MPJLambdaWrapper<Alarm>()
                .innerJoin(Algorithm.class, Algorithm::getId, Alarm::getAlgorithmId)
                .selectAll(Alarm.class)
                .selectAs(Algorithm::getKey, AlarmVO::getAlgorithmKey)
                .selectAs(Algorithm::getName, AlarmVO::getAlgorithmName)
                .eq(StrUtil.isNotBlank(param.getAlgorithmId()), Alarm::getAlgorithmId, param.getAlgorithmId())
                .like(StrUtil.isNotBlank(param.getAlarmMsg()), Alarm::getAlarmMsg, param.getAlarmMsg())
                .eq(Objects.nonNull(param.getStatus()), Alarm::getStatus, param.getStatus())
                .ge(Objects.nonNull(param.getStartTime()), Alarm::getStartTime, param.getStartTime())
                .le(Objects.nonNull(param.getEndTime()), Alarm::getStartTime, param.getEndTime())
                .orderByDesc(Alarm::getStartTime));
    }

    @Override
    public AlarmVO getInfo(String id) {
        return alarmMapper.selectJoinOne(AlarmVO.class, new MPJLambdaWrapper<Alarm>()
                .innerJoin(Algorithm.class, Algorithm::getId, Alarm::getAlgorithmId)
                .selectAs(Algorithm::getKey, AlarmVO::getAlgorithmKey)
                .selectAs(Algorithm::getName, AlarmVO::getAlgorithmName)
                .eq(Alarm::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        List<String> alarmIds = Arrays.stream(ids).collect(Collectors.toList());
        alarmImageMapper.deleteBatchIds(alarmIds);
        alarmImageMapper.delete(new LambdaQueryWrapper<AlarmImage>().in(AlarmImage::getAlarmId, alarmIds));
    }

    @Override
    public void realTimeAlarm(RealTimeAlarmDTO param) throws IOException {
        // 从缓存获取算法（线程安全）
        Algorithm algorithm = algorithmMap.get(param.getAlgorithmKey());
        if (Objects.isNull(algorithm)) {
            log.info("未找到启用的算法：{}", param.getAlgorithmKey());
            return;
        }

        if (param.getAlarmStatus()) {
            handleAlarmStart(param);
        } else {
            handleAlarmEnd(param);
        }
    }

    @Override
    public CountAlarmInfoVO countAlarmInfo() {
        long alarmTotalCount = alarmMapper.queryAlarmTotalCount();
        List<AlgorithmAlarmBO> algorithmAlarmCount = alarmMapper.queryAlgorithmAlarmCount();
        return new CountAlarmInfoVO(alarmTotalCount, algorithmAlarmCount);
    }

    private static final String ALARM_INFO_KEY = "alarmInfo:";
    private static final String ALARM_IMAGE_KEY = "alarmImage:";
    private static final int CACHE_EXPIRE_HOURS = 24;

    /**
     * 服务启动时初始化算法缓存（消除冷启动+并发问题）
     */
    @PostConstruct
    public void loadData() {
        // 算法缓存为空，加载算法缓存
        if (algorithmMap.isEmpty()) {
            List<Algorithm> algorithmList = algorithmMapper.selectList(new LambdaQueryWrapper<Algorithm>()
                    .select(Algorithm::getId, Algorithm::getKey, Algorithm::getName)
                    .eq(Algorithm::getEnable, Boolean.TRUE));
            for (Algorithm algorithm : algorithmList) {
                algorithmMap.put(algorithm.getKey(), algorithm);
                idAlgorithmMap.put(algorithm.getId(), algorithm);
            }
            log.info("算法缓存初始化完成，共加载【{}】个算法", algorithmList.size());
        }
    }

    /**
     * 处理发起告警消息
     *
     * @param param 告警报文
     */
    private void handleAlarmStart(RealTimeAlarmDTO param) {
        // 生成Redis Key
        String alarmInfoKey = ALARM_INFO_KEY + param.getAlgorithmKey();
        String alarmImageKey = ALARM_IMAGE_KEY + param.getAlgorithmKey();
        // 生成告警信息
        Alarm alarm = new Alarm();
        alarm.setAlgorithmId(algorithmMap.get(param.getAlgorithmKey()).getId());
        alarm.setAlarmMsg(getAlarmMsg(param.getAlgorithmKey()));
        alarm.setStartTime(param.getAlarmTime());
        alarm.setStatus(AlarmStatus.INITIATE);
        // 缓存到Redis
        stringHandler.set(alarmInfoKey, alarm, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
        stringHandler.set(alarmImageKey, param.getImgs(), CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
    }

    /**
     * 处理取消告警消息
     *
     * @param param 告警报文
     */
    private void handleAlarmEnd(RealTimeAlarmDTO param) throws IOException {
        // 生成Redis Key
        String alarmInfoKey = ALARM_INFO_KEY + param.getAlgorithmKey();
        String alarmImageKey = ALARM_IMAGE_KEY + param.getAlgorithmKey();
        // 取消告警信息
        Alarm alarm = (Alarm) stringHandler.get(alarmInfoKey);
        alarm.setEndTime(param.getAlarmTime());
        alarm.setStatus(AlarmStatus.CANCEL);
        alarmMapper.insert(alarm);

        // 从Redis缓存获取告警图片
        List<String> imgs = (List<String>) stringHandler.get(alarmImageKey);

        // 异步处理图片（IO密集型操作异步化，主线程快速返回）
        asyncHandleAlarmImages(alarm.getId(), imgs);

        // 删除Redis缓存
        stringHandler.delete(alarmInfoKey);
        stringHandler.delete(alarmImageKey);
    }

    /**
     * 异步处理图片：Base64转文件 + 批量插入数据库
     *
     * @param alarmId 告警记录ID
     * @param imgs    Base64图片列表
     */
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

    /**
     * 核心方法：扫描Redis中指定前缀的key，筛选正在告警的Alarm对象
     */
    private List<Alarm> scanActiveAlarmFromRedis() {
        List<Alarm> activeAlarmList = new ArrayList<>();

        // 执行scan扫描（迭代式获取key，避免阻塞Redis）
        List<String> keys = stringHandler.scanKeysWithPrefix(ALARM_INFO_KEY);
        keys.forEach(key -> {
            try {
                // 读取key对应的value（反序列化为Alarm对象）
                Object value = stringHandler.get(key);
                if (value instanceof Alarm) {
                    Alarm alarm = (Alarm) value;
                    // 筛选：状态为「正在告警」（INITIATE）的记录
                    if (AlarmStatus.INITIATE.equals(alarm.getStatus())) {
                        activeAlarmList.add(alarm);
                    }
                }
            } catch (Exception e) {
                // 异常处理：避免单条数据反序列化失败影响整体扫描
                log.error("扫描Redis告警数据失败，key：{}，错误：{}", key, e.getMessage());
            }
        });

        return activeAlarmList;
    }

    /**
     * 根据算法标识获取告警消息
     *
     * @param algorithmKey 算法标识
     * @return 告警消息
     */
    private String getAlarmMsg(String algorithmKey) {
        switch (algorithmKey) {
            case "person":
                return "算法【人员检测】检测到人员";
            case "hat":
                return "算法【安全帽检测】检测到未佩戴安全帽";
            case "post":
                return "算法【脱岗检测】检测到人员脱岗";
            case "fire":
                return "算法【火灾检测】检测到火灾";
            default:
                return "未知";
        }
    }

    @Override
    public Page<AlarmVO> mergeSearchByPage(AlarmQueryDTO param) {
        // 1. 步骤1：查询数据库中的告警数据（保持原有逻辑不变
        Page<AlarmVO> pageResult = this.searchByPage(param);
        List<AlarmVO> dbAlarmList = pageResult.getRecords(); // 数据库查询结果列表
        Long dbTotal = pageResult.getTotal(); // 数据库总条数

        // 2. 步骤2：查询Redis中符合条件的实时告警数据（仅INITIATE状态）
        List<AlarmVO> redisAlarmList = getRedisAlarmListByParam(param);

        // 3. 步骤3：合并数据（去重 + 统一排序）
        List<AlarmVO> mergedList = mergeAndDeduplicate(dbAlarmList, redisAlarmList);

        // 4. 步骤4：重新构建分页结果（基于合并后的数据）
        return buildMergedPage(mergedList, param.getPage());
    }

    /**
     * 从Redis中获取符合查询条件的实时告警VO列表
     */
    private List<AlarmVO> getRedisAlarmListByParam(AlarmQueryDTO param) {
        List<AlarmVO> redisAlarmVOList = new ArrayList<>();

        // 提前判断：如果查询条件指定状态不是INITIATE，Redis中无对应数据，直接返回空
        if (Objects.nonNull(param.getStatus()) && !AlarmStatus.INITIATE.equals(param.getStatus())) {
            return redisAlarmVOList;
        }

        // 迭代扫描Redis key
        stringHandler.scanKeysWithPrefix(ALARM_INFO_KEY).forEach(key -> {
            try {
                // 反序列化Redis中的Alarm对象
                Object value = stringHandler.get(key);
                if (value instanceof Alarm) {
                    Alarm redisAlarm = (Alarm) value;
                    // 过滤：仅保留正在告警状态（Redis中理论上只有该状态，但双重校验）
                    if (AlarmStatus.INITIATE.equals(redisAlarm.getStatus())) {
                        // 应用查询条件筛选（与数据库筛选逻辑一致）
                        if (matchQueryParam(redisAlarm, param)) {
                            // 转换为AlarmVO（补充算法key和name，从algorithmMap获取）
                            AlarmVO alarmVO = convertToAlarmVO(redisAlarm);
                            redisAlarmVOList.add(alarmVO);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Redis扫描告警数据失败，key：{}，错误：{}", key, e.getMessage());
            }
        });

        return redisAlarmVOList;
    }

    /**
     * 校验Redis中的Alarm是否符合AlarmQueryDTO的筛选条件
     */
    private boolean matchQueryParam(Alarm alarm, AlarmQueryDTO param) {
        // 1. 算法ID筛选
        if (StrUtil.isNotBlank(param.getAlgorithmId())
                && !param.getAlgorithmId().equals(alarm.getAlgorithmId())) {
            return false;
        }
        // 2. 告警信息模糊筛选
        if (StrUtil.isNotBlank(param.getAlarmMsg())
                && !alarm.getAlarmMsg().contains(param.getAlarmMsg())) {
            return false;
        }
        // 3. 时间范围筛选（开始时间 >= param.startTime 且 <= param.endTime）
        if (Objects.nonNull(param.getStartTime())
                && alarm.getStartTime().before(param.getStartTime())) {
            return false;
        }
        if (Objects.nonNull(param.getEndTime())
                && alarm.getStartTime().after(param.getEndTime())) {
            return false;
        }
        // 其他筛选条件可按需添加（如状态已在前面过滤）
        return true;
    }

    /**
     * 将Redis中的Alarm对象转换为AlarmVO（补充算法信息）
     */
    private AlarmVO convertToAlarmVO(Alarm alarm) {
        AlarmVO vo = new AlarmVO();
        // 复制Alarm的基础字段（id、algorithmId、alarmMsg、startTime、status等）
        vo.setId(StrUtil.isEmpty(alarm.getId()) ? alarm.getAlgorithmId() : alarm.getId());
        vo.setAlgorithmId(alarm.getAlgorithmId());
        vo.setAlarmMsg(alarm.getAlarmMsg());
        vo.setStartTime(alarm.getStartTime());
        vo.setStatus(alarm.getStatus());
        // 从idAlgorithmMap补充算法key和name（无需查数据库）
        Algorithm algorithm = idAlgorithmMap.get(alarm.getAlgorithmId()); // 假设Alarm有algorithmId字段（之前缓存时存储了）
        if (algorithm != null) {
            vo.setAlgorithmKey(algorithm.getKey());
            vo.setAlgorithmName(algorithm.getName());
        }
        // 排序字段：Redis中的数据用startTime作为createTime（保持排序一致）
        //vo.setCreateTime(alarm.getStartTime());
        return vo;
    }

    /**
     * 合并数据库和Redis的数据，按告警ID去重，按createTime降序排序
     */
    private List<AlarmVO> mergeAndDeduplicate(List<AlarmVO> dbList, List<AlarmVO> redisList) {
        // 1. 用LinkedHashMap去重（key=告警ID，value=AlarmVO，保留先出现的数据库数据）
        Map<String, AlarmVO> deduplicateMap = new LinkedHashMap<>();

        // 先添加数据库数据（数据库数据优先级高于Redis，避免重复）
        dbList.forEach(vo -> deduplicateMap.put(vo.getId(), vo));

        // 再添加Redis数据（仅添加数据库中没有的告警ID）
        redisList.forEach(vo -> deduplicateMap.putIfAbsent(vo.getId(), vo));

        // 2. 按createTime降序排序（与原有逻辑一致）
        return deduplicateMap.values().stream()
                .sorted((v1, v2) -> v2.getStartTime().compareTo(v1.getStartTime()))
                .collect(Collectors.toList());
    }

    /**
     * 基于合并后的数据，重新构建分页结果
     */
    private Page<AlarmVO> buildMergedPage(List<AlarmVO> mergedList, Page<AlarmVO> originalPage) {
        Page<AlarmVO> resultPage = new Page<>();
        int pageNum = (int) originalPage.getCurrent(); // 当前页码（从1开始）
        int pageSize = (int) originalPage.getSize(); // 每页条数

        // 1. 计算总条数和总页数
        long total = mergedList.size();
        long totalPages = (long) Math.ceil((double) total / pageSize);

        // 2. 截取当前页数据（避免数组越界）
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, mergedList.size());
        List<AlarmVO> currentPageList = mergedList.subList(startIndex, endIndex);

        // 3. 封装分页结果
        resultPage.setCurrent(pageNum);
        resultPage.setSize(pageSize);
        resultPage.setTotal(total);
        resultPage.setPages(totalPages);
        resultPage.setRecords(currentPageList);

        return resultPage;
    }

}
