package com.maxinhai.platform.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.maxinhai.platform.bo.TaskOrderProcessSortBO;
import com.maxinhai.platform.dto.OrderAddDTO;
import com.maxinhai.platform.feign.SystemFeignClient;
import com.maxinhai.platform.handler.StringHandler;
import com.maxinhai.platform.mapper.TaskOrderMapper;
import com.maxinhai.platform.mapper.UserMapper;
import com.maxinhai.platform.po.TaskOrder;
import com.maxinhai.platform.po.User;
import com.maxinhai.platform.service.DistributeTaskOrderToRandomUserService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.ClientInfoUtils;
import com.maxinhai.platform.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DistributeTaskOrderToRandomUserServiceImpl implements DistributeTaskOrderToRandomUserService {

    @Resource
    @Qualifier("ioIntensiveExecutor")
    public Executor ioIntensiveExecutor;
    @Value("${spring.profiles.active}")
    private String env;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private SystemFeignClient systemFeign;
    @Resource
    private UserMapper userMapper;
    @Resource
    private TaskOrderMapper taskOrderMapper;
    @Resource
    private StringHandler stringHandler;
    @Resource
    private JwtUtils jwtUtils;

    // 在线用户
    private static final List<User> onlineUserList = new ArrayList<>();
    // 在线用户token
    private static final ConcurrentHashMap<String, String> userTokenMap = new ConcurrentHashMap<>();
    // 在线用户->派工单
    private static final ConcurrentHashMap<String, String> userTaskMap = new ConcurrentHashMap<>();
    // 在线用户->流程步骤
    private static final ConcurrentHashMap<String, String> userStepMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void loadData() {
        List<User> userList = getRandomUsers(100);
        onlineUserList.addAll(userList);
        for (User user : userList) {
            String account = user.getAccount();
            Object token = stringHandler.get("auth:token:" + account);
            if (token != null) {
                userTokenMap.put(account, token.toString());
            }
        }
        log.info("加载用户token完毕");
    }

    @Scheduled(initialDelay = 10000, fixedDelay = 60 * 1000)
    public void handler() {
//        for (User user : onlineUserList) {
//            distribute(user.getAccount());
//        }
//        handOut();
        operateTaskOrder();
    }

    @Override
    public List<User> getRandomUsers(int count) {
        Long userCount = userMapper.selectCount(new LambdaQueryWrapper<>());
        int randomIndex = RandomUtil.randomInt(1, userCount.intValue() - count, true, true);
        return userMapper.selectUserList(count, randomIndex);
    }

    /**
     * 给在线用户派发派工单
     *
     * @param account 账号
     */
    public void distribute(String account) {
        boolean loginFlag = userTokenMap.containsKey(account);
        String orderStatus = userStepMap.getOrDefault(account, "wait"); // 订单状态：wait.待分配 start.待开工 pause.待暂停 resume.待复工 report.待报工 none.没有派工单分配
        // 登录账户
        if (!loginFlag) {
            AjaxResult result = login(account);
            if (result.getCode() != HttpStatus.OK.value()) {
                ioIntensiveExecutor.execute(() -> register(account));
                log.error("登录 账号:{} -> 调用结果: {}", account, result);
            } else {
                userTokenMap.put(account, result.getData().toString());
                orderStatus = "wait";
                userStepMap.put(account, orderStatus);
            }
        } else if (loginFlag && "wait".equals(orderStatus)) {
            // 指派派工单给在线用户
            List<TaskOrder> taskOrderList = queryCanStartWorkTaskList();
            if (CollectionUtils.isEmpty(taskOrderList)) {
                userStepMap.put(account, "none");
                return;
            }
            // 按sort顺序遍历派工单，找到第一个未被分配的
            boolean assigned = false;
            for (TaskOrder taskOrder : taskOrderList) {
                String taskOrderId = taskOrder.getId();
                Collection<String> values = userTaskMap.values();
                if (!values.contains(taskOrderId)) {
                    userTaskMap.put(account, taskOrderId);
                    switch (taskOrder.getStatus()) {
                        case INIT:
                            userStepMap.put(account, "start");
                            break;
                        case START:
                            userStepMap.put(account, "pause");
                            break;
                        case PAUSE:
                            userStepMap.put(account, "resume");
                            break;
                        case RESUME:
                            userStepMap.put(account, "report");
                            break;
                        default:
                            break;
                    }
                    // 找到第一个未分配的，立即终止循环，避免覆盖
                    assigned = true;
                    break;
                }
            }

            // 如果没有找到可分配的派工单
            if (!assigned) {
                userStepMap.put(account, "none");
            }
        } else if (loginFlag && "none".equals(orderStatus)) {
            // 创建订单
            handleJwtExpired(account, createOrder(account));
            userStepMap.put(account, "wait");
        } else if (loginFlag && "start".equals(orderStatus)) {
            // 开工
            handleJwtExpired(account, startWork(account, userTaskMap.get(account)));
            // 根据生成随机数，判断下一步是暂停还是报工
            int randomNum = RandomUtil.randomInt(1, 10);
            userStepMap.put(account, randomNum >= 3 ? "pause" : "report");
        } else if (loginFlag && "pause".equals(orderStatus)) {
            // 停工
            handleJwtExpired(account, pauseWork(account, userTaskMap.get(account)));
            userStepMap.put(account, "resume");
        } else if (loginFlag && "resume".equals(orderStatus)) {
            // 复工
            handleJwtExpired(account, resumeWork(account, userTaskMap.get(account)));
            userStepMap.put(account, "report");
        } else if (loginFlag && "report".equals(orderStatus)) {
            // 报工
            handleJwtExpired(account, reportWork(account, userTaskMap.get(account)));
            userTaskMap.remove(account);
            userStepMap.put(account, "wait");
        } else {
            throw new RuntimeException("未知状态");
        }
        log.info("派发用户: {}, 派工单: {}, 状态: {}", account, userTaskMap.get(account), orderStatus);
    }

    private static final ConcurrentHashMap<String, String> tokenMap = new ConcurrentHashMap<>();

    public void handOut() {
        // 原本逻辑错了，应该现有任务在找人去做，而不是先找100个人，再去找派工单做
        // 1.先按照创建时间升序找未完工的派工单
        List<TaskOrder> taskOrderList = taskOrderMapper.queryCanStartTaskListByCount(100);
        if (CollectionUtils.isEmpty(taskOrderList)) {
            return;
        }
        Integer sort = taskOrderList.get(0).getSort();
        List<TaskOrder> canStartWorkTaskList = taskOrderList.stream()
                .filter(taskOrder -> Objects.nonNull(taskOrder.getSort())
                        && Objects.nonNull(sort)
                        && taskOrder.getSort().equals(sort))
                .collect(Collectors.toList());
        // 2.根据同一道工序未完工派工单数量获取随机用户
        if (CollectionUtils.isEmpty(tokenMap) || tokenMap.size() < canStartWorkTaskList.size()) {
            int canStartWorkTaskCount = canStartWorkTaskList.size() - tokenMap.size();
            List<User> randomUsers = getRandomUsers(canStartWorkTaskCount);
            for (User randomUser : randomUsers) {
                String account = randomUser.getAccount();
                AjaxResult result = login(account);
                if (result.getCode() == HttpStatus.OK.value()) {
                    tokenMap.putIfAbsent(account, result.getData().toString());
                }
            }
        }
        List<String> tokens = tokenMap.values().stream().collect(Collectors.toList());
        // 3.操作未完工派工单
        for (int i = 0; i < canStartWorkTaskList.size(); i++) {
            TaskOrder taskOrder = canStartWorkTaskList.get(i);
            String token = tokens.get(i);
            AjaxResult ajaxResult = null;
            switch (taskOrder.getStatus()) {
                case INIT:
                    ajaxResult = startWorkWithToken(token, taskOrder.getId());
                    break;
                case START:
                    ajaxResult = pauseWorkWithToken(token, taskOrder.getId());
                    break;
                case PAUSE:
                    ajaxResult = resumeWorkWithToken(token, taskOrder.getId());
                    break;
                case RESUME:
                    ajaxResult = reportWorkWithToken(token, taskOrder.getId());
                    break;
                default:
                    break;
            }
            if (Objects.nonNull(ajaxResult) && ajaxResult.getCode() != HttpStatus.OK.value()) {
                log.info("派工单操作报错 => 状态码:{},消息:{},数据:{}",
                        ajaxResult.getCode(), ajaxResult.getMsg(), ajaxResult.getData());
            }
        }
    }

    public void operateTaskOrder() {
        // 1.加载token
        List<String> tokenKeys = stringHandler.scanKeysWithPrefix("auth:token:");
        if (CollectionUtils.isEmpty(tokenKeys) || tokenKeys.size() < 300) {
            List<User> randomUsers = getRandomUsers(300 - tokenKeys.size());
            for (User randomUser : randomUsers) {
                String account = randomUser.getAccount();
                AjaxResult result = login(account);
                if (result.getCode() == HttpStatus.OK.value()) {
                    log.info("账号：{}登录成功", account);
                }
            }
            tokenKeys = stringHandler.scanKeysWithPrefix("auth:token:");
        }
        // 2.查询派工单中的工序顺序号
        List<TaskOrderProcessSortBO> processSortBOList = taskOrderMapper.selectProcessSort();
        for (TaskOrderProcessSortBO processSortBO : processSortBOList) {
            // 3.根据工序顺序号查找可操作的派工单集合
            Integer sort = processSortBO.getSort();
            List<TaskOrder> taskOrderList = taskOrderMapper.selectOperableTaskOrderList(sort);
            log.info("工序顺序号:{}, 派工单数量:{}", sort, taskOrderList.size());
            // 4.操作未完工派工单
            int tokenIndex = 0;
            for (int i = 0; i < taskOrderList.size(); i++) {
                TaskOrder taskOrder = taskOrderList.get(i);
                if(tokenIndex >= tokenKeys.size()) {
                    tokenIndex = 0;
                }
                String tokenKey = tokenKeys.get(tokenIndex);
                String token = stringHandler.get(tokenKey).toString();
                Boolean tokenExpired = jwtUtils.isTokenExpired(token);
                if(tokenExpired) {
                    String account = jwtUtils.getAccountFromToken(token);
                    AjaxResult result = login(account);
                    if (result.getCode() == HttpStatus.OK.value()) {
                        log.info("校验机制 -> 账号：{}登录成功", account);
                        token = stringHandler.get(tokenKey).toString();
                    }
                }
                AjaxResult ajaxResult = null;
                switch (taskOrder.getStatus()) {
                    case INIT:
                        ajaxResult = startWorkWithToken(token, taskOrder.getId());
                        break;
                    case START:
                        ajaxResult = pauseWorkWithToken(token, taskOrder.getId());
                        break;
                    case PAUSE:
                        ajaxResult = resumeWorkWithToken(token, taskOrder.getId());
                        break;
                    case RESUME:
                        ajaxResult = reportWorkWithToken(token, taskOrder.getId());
                        break;
                    default:
                        break;
                }
                if (Objects.nonNull(ajaxResult) && ajaxResult.getCode() != HttpStatus.OK.value()) {
                    log.info("派工单操作报错 => 状态码:{},消息:{},数据:{}",
                            ajaxResult.getCode(), ajaxResult.getMsg(), ajaxResult.getData());
                }
                tokenIndex++;
            }
        }
    }

    /**
     * 处理token过期
     *
     * @param account    账号
     * @param ajaxResult
     */
    private void handleJwtExpired(String account, AjaxResult ajaxResult) {
        if (ajaxResult.getCode() == HttpStatus.UNAUTHORIZED.value()) {
            userTokenMap.remove(account);
        }
        if (ajaxResult.getCode() == HttpStatus.SERVICE_UNAVAILABLE.value()) {
            userTokenMap.remove(account);
        }
    }

    public AjaxResult login(String account) {
        // 构建登录账号参数
        JSONObject param = new JSONObject();
        param.put("account", account);
        param.put("password", "123456");

        // 创建请求头对象
        HttpHeaders headers = new HttpHeaders();
        // 构造请求头，手动添加 User-Agent
        headers.add("User-Agent", ClientInfoUtils.getRandomUserAgent());
        // 设置请求头（根据需要添加，例如Token、User-Agent等）
        headers.add("sa-token", "internal");
        // 封装请求头和请求参数（GET请求无请求体，可传null）
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(param, headers);
        AjaxResult ajaxResult = restTemplate.exchange("http://" + judgeEnv(env, "system") + ":10010/api/auth/login", HttpMethod.POST, requestEntity, AjaxResult.class).getBody();
        return ajaxResult;
    }

    public AjaxResult register(String account) {
        // 构建登录账号参数
        JSONObject param = new JSONObject();
        param.put("account", account);
        param.put("username", account);
        param.put("password", "123456");
        param.put("phone", "15735400536");

        String token = login("caocao").getData().toString();

        // 创建请求头对象
        HttpHeaders headers = new HttpHeaders();
        // 构造请求头，手动添加 User-Agent
        headers.add("User-Agent", ClientInfoUtils.getRandomUserAgent());
        // 设置请求头（根据需要添加，例如Token、User-Agent等）
        headers.add("sa-token", "internal");
        headers.add("Authorization", token);
        // 封装请求头和请求参数（GET请求无请求体，可传null）
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(param, headers);
        AjaxResult ajaxResult = restTemplate.exchange("http://" + judgeEnv(env, "system") + ":10010/api/auth/register", HttpMethod.POST, requestEntity, AjaxResult.class).getBody();
        return ajaxResult;
    }

    /**
     * 查询工单下可以开工、暂停、复工、报工的派工单集合
     *
     * @return 可以开工、暂停、复工、报工的派工单集合
     */
    public List<TaskOrder> queryCanStartWorkTaskList() {
        return taskOrderMapper.queryCanStartTaskList();
    }

    /**
     * 创建订单
     */
    public AjaxResult createOrder(String account) {
        AjaxResult<List<String>> ajaxResult = systemFeign.generateCode("order", 1);
        if (ajaxResult.getCode() != HttpStatus.OK.value()) {
            log.error("生成订单编码出错: {}", ajaxResult.getMsg());
            return ajaxResult;
        }

        // 构建创建订单参数
        OrderAddDTO param = new OrderAddDTO();
        param.setOrderCode(ajaxResult.getData().get(0));
        param.setQty(100);
        param.setOrderType(1);
        param.setPlanBeginTime(new Date());
        param.setPlanEndTime(DateUtil.offsetDay(param.getPlanBeginTime(), 7));
        param.setProductId("1971886706000953346");
        param.setBomId("1971893744164794369");
        param.setRoutingId("1971916261298331650");

        // 创建请求头对象
        HttpHeaders headers = new HttpHeaders();
        // 构造请求头，手动添加 User-Agent
        headers.add("User-Agent", ClientInfoUtils.getRandomUserAgent());
        // 设置请求头（根据需要添加，例如Token、User-Agent等）
        headers.add("sa-token", "internal");
        headers.add("Authorization", userTokenMap.get(account));
        // 封装请求头和请求参数（GET请求无请求体，可传null）
        HttpEntity<OrderAddDTO> requestEntity = new HttpEntity<>(param, headers);
        ajaxResult = restTemplate.exchange("http://" + judgeEnv(env, "produce") + ":10040/order/addOrder", HttpMethod.POST, requestEntity, AjaxResult.class).getBody();
        return ajaxResult;
    }

    /**
     * 派工单-开工
     */
    public AjaxResult startWork(String account, String taskOrderId) {
        // 创建请求头对象
        HttpHeaders headers = new HttpHeaders();
        // 构造请求头，手动添加 User-Agent
        headers.add("User-Agent", ClientInfoUtils.getRandomUserAgent());
        // 设置请求头（根据需要添加，例如Token、User-Agent等）
        headers.add("sa-token", "internal");
        headers.add("Authorization", userTokenMap.get(account));
        // 封装请求头和请求参数（GET请求无请求体，可传null）
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        AjaxResult ajaxResult = restTemplate.exchange("http://" + judgeEnv(env, "produce") + ":10040/taskOrder/startWork/{taskOrderId}", HttpMethod.GET, requestEntity, AjaxResult.class, taskOrderId).getBody();
        return ajaxResult;
    }

    public AjaxResult startWorkWithToken(String token, String taskOrderId) {
        // 创建请求头对象
        HttpHeaders headers = new HttpHeaders();
        // 构造请求头，手动添加 User-Agent
        headers.add("User-Agent", ClientInfoUtils.getRandomUserAgent());
        // 设置请求头（根据需要添加，例如Token、User-Agent等）
        headers.add("sa-token", "internal");
        headers.add("Authorization", token);
        // 封装请求头和请求参数（GET请求无请求体，可传null）
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        AjaxResult ajaxResult = restTemplate.exchange("http://" + judgeEnv(env, "produce") + ":10040/taskOrder/startWork/{taskOrderId}", HttpMethod.GET, requestEntity, AjaxResult.class, taskOrderId).getBody();
        return ajaxResult;
    }

    /**
     * 派工单-暂停
     */
    public AjaxResult pauseWork(String account, String taskOrderId) {
        // 创建请求头对象
        HttpHeaders headers = new HttpHeaders();
        // 构造请求头，手动添加 User-Agent
        headers.add("User-Agent", ClientInfoUtils.getRandomUserAgent());
        // 设置请求头（根据需要添加，例如Token、User-Agent等）
        headers.add("sa-token", "internal");
        headers.add("Authorization", userTokenMap.get(account));
        // 封装请求头和请求参数（GET请求无请求体，可传null）
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        AjaxResult ajaxResult = restTemplate.exchange("http://" + judgeEnv(env, "produce") + ":10040/taskOrder/pauseWork/{taskOrderId}", HttpMethod.GET, requestEntity, AjaxResult.class, taskOrderId).getBody();
        return ajaxResult;
    }

    public AjaxResult pauseWorkWithToken(String token, String taskOrderId) {
        // 创建请求头对象
        HttpHeaders headers = new HttpHeaders();
        // 构造请求头，手动添加 User-Agent
        headers.add("User-Agent", ClientInfoUtils.getRandomUserAgent());
        // 设置请求头（根据需要添加，例如Token、User-Agent等）
        headers.add("sa-token", "internal");
        headers.add("Authorization", token);
        // 封装请求头和请求参数（GET请求无请求体，可传null）
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        AjaxResult ajaxResult = restTemplate.exchange("http://" + judgeEnv(env, "produce") + ":10040/taskOrder/pauseWork/{taskOrderId}", HttpMethod.GET, requestEntity, AjaxResult.class, taskOrderId).getBody();
        return ajaxResult;
    }


    /**
     * 派工单-复工
     */
    public AjaxResult resumeWork(String account, String taskOrderId) {
        // 创建请求头对象
        HttpHeaders headers = new HttpHeaders();
        // 构造请求头，手动添加 User-Agent
        headers.add("User-Agent", ClientInfoUtils.getRandomUserAgent());
        // 设置请求头（根据需要添加，例如Token、User-Agent等）
        headers.add("sa-token", "internal");
        headers.add("Authorization", userTokenMap.get(account));
        // 封装请求头和请求参数（GET请求无请求体，可传null）
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        AjaxResult ajaxResult = restTemplate.exchange("http://" + judgeEnv(env, "produce") + ":10040/taskOrder/resumeWork/{taskOrderId}", HttpMethod.GET, requestEntity, AjaxResult.class, taskOrderId).getBody();
        return ajaxResult;
    }

    public AjaxResult resumeWorkWithToken(String token, String taskOrderId) {
        // 创建请求头对象
        HttpHeaders headers = new HttpHeaders();
        // 构造请求头，手动添加 User-Agent
        headers.add("User-Agent", ClientInfoUtils.getRandomUserAgent());
        // 设置请求头（根据需要添加，例如Token、User-Agent等）
        headers.add("sa-token", "internal");
        headers.add("Authorization", token);
        // 封装请求头和请求参数（GET请求无请求体，可传null）
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        AjaxResult ajaxResult = restTemplate.exchange("http://" + judgeEnv(env, "produce") + ":10040/taskOrder/resumeWork/{taskOrderId}", HttpMethod.GET, requestEntity, AjaxResult.class, taskOrderId).getBody();
        return ajaxResult;
    }

    /**
     * 派工单-报工
     */
    public AjaxResult reportWork(String account, String taskOrderId) {
        // 创建请求头对象
        HttpHeaders headers = new HttpHeaders();
        // 构造请求头，手动添加 User-Agent
        headers.add("User-Agent", ClientInfoUtils.getRandomUserAgent());
        // 设置请求头（根据需要添加，例如Token、User-Agent等）
        headers.add("sa-token", "internal");
        headers.add("Authorization", userTokenMap.get(account));
        // 封装请求头和请求参数（GET请求无请求体，可传null）
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        AjaxResult ajaxResult = restTemplate.exchange("http://" + judgeEnv(env, "produce") + ":10040/taskOrder/reportWork/{taskOrderId}", HttpMethod.GET, requestEntity, AjaxResult.class, taskOrderId).getBody();
        return ajaxResult;
    }

    public AjaxResult reportWorkWithToken(String token, String taskOrderId) {
        // 创建请求头对象
        HttpHeaders headers = new HttpHeaders();
        // 构造请求头，手动添加 User-Agent
        headers.add("User-Agent", ClientInfoUtils.getRandomUserAgent());
        // 设置请求头（根据需要添加，例如Token、User-Agent等）
        headers.add("sa-token", "internal");
        headers.add("Authorization", token);
        // 封装请求头和请求参数（GET请求无请求体，可传null）
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        AjaxResult ajaxResult = restTemplate.exchange("http://" + judgeEnv(env, "produce") + ":10040/taskOrder/reportWork/{taskOrderId}", HttpMethod.GET, requestEntity, AjaxResult.class, taskOrderId).getBody();
        return ajaxResult;
    }

    /**
     * 根据环境判断使用localhost还是容器名
     *
     * @param env         环境
     * @param serviceName 服务名称
     * @return
     */
    public String judgeEnv(String env, String serviceName) {
        String container = null;
        switch (env) {
            case "dev":
                container = "localhost";
                break;
            case "prod":
                container = "cloud-platform-" + serviceName;
                break;
            default:
                throw new RuntimeException("Invalid env: " + env);
        }
        return container;
    }
}
