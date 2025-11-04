package com.maxinhai.platform;

import com.maxinhai.platform.dto.AlarmInitiateDTO;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.ConcurrentApiInvoker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@SpringBootTest
public class FileUploadTest {

    @Test
    public void test() throws IOException, InterruptedException {
//        callInitiateApi();
        String[] array = {"person", "hat", "fire"};
        boolean alarmFlag = true;
        for (int i = 0; i < 100; i++) {
            for (String item : array) {
                AlarmInitiateDTO initiateDTO = new AlarmInitiateDTO();
                initiateDTO.setAlgorithmType(item);
                initiateDTO.setAlarmFlag(alarmFlag);
                initiateDTO.setImage(convert("C:\\Users\\MaXinHai\\Pictures\\微信图片_20250828225709.jpg"));
                initiateDTO.setBeginTime(new Date());
                initiateDTO.setEndTime(new Date());
                sendAlarm("http://localhost:20013/alarmInfo/initiate", initiateDTO);
            }
            alarmFlag = !alarmFlag;
            TimeUnit.SECONDS.sleep(1);
        }
    }

    private static boolean flag = false;

    public void callInitiateApi() {
        flag = !flag;
        // 1. 定义接口调用逻辑（这里模拟一个带延迟的接口）
        Supplier<String> mockApiCall = () -> {
            try {
                AjaxResult<Void> ajaxResult = sendFormData("person", flag);
                sendFormData("hat", flag);
                sendFormData("fire", flag);
                // 返回当前线程名（用于验证并发）
                return "接口返回结果 - 线程: " + Thread.currentThread().getName() + " -> 调用结果: " + ajaxResult;
            } catch (Exception e) {
                log.error("调用接口报错: {}", e.getMessage());
                Thread.currentThread().interrupt(); // 恢复中断状态
                throw new RuntimeException("接口调用被中断", e);
            }
        };

        // 2. 并发调用5次接口
        int callCount = 1;
        List<String> results = null;
        try {
            results = ConcurrentApiInvoker.invokeConcurrently(callCount, mockApiCall);
        } catch (Exception e) {
            log.error("调用API报错: {}", e.getMessage());
        }

        // 3. 打印结果
        log.info("===== 所有接口调用完成 =====");
        results.forEach(log::info);
    }

    /**
     * 发送POST请求，FORM-DATA格式
     *
     * @return
     * @throws Exception
     */
    public AjaxResult<Void> sendFormData(String algorithmType, boolean alarmFlag) throws Exception {
        // 目标接口URL
        String url = "http://localhost:20013/alarmInfo/initiate";

        // 1. 构建form-data数据（MultiValueMap）
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();

        // 添加普通键值对（表单字段名 -> 值）
        formData.add("algorithmType", algorithmType); // 算法类型
        formData.add("alarmFlag", alarmFlag);         // 告警标记

        // 本地图片路径
        String filePath = "C:\\Users\\MaXinHai\\Pictures\\微信图片_20250828225709.jpg";
        // 读取本地文件创建MockMultipartFile
//        MockMultipartFile imageFile = new MockMultipartFile(
//                "image",  // 表单字段名（需与接口@RequestParam("image")一致）
//                new File(filePath).getName(),  // 原始文件名
//                MediaType.IMAGE_JPEG_VALUE,    // MIME类型（image/jpeg）
//                new FileInputStream(filePath)  // 文件输入流（推荐大文件用流，小文件可用字节数组）
//        );
//        formData.add("image", imageFile);  // 添加文件到form-data，表单字段名为"image"
        formData.add("image", convert(filePath));  // 添加文件到form-data，表单字段名为"image"

        // 2. 发送POST请求（form-data格式）
        RestTemplate restTemplate = new RestTemplate();

        // 3. 发送POST请求（指定Content-Type为multipart/form-data）
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);  // 显式指定类型
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(formData, headers);
        ResponseEntity<AjaxResult> response = restTemplate.postForEntity(
                url,             // 请求URL
                requestEntity,   // 请求体（form-data数据）
                AjaxResult.class // 响应数据类型
        );

        return response.getBody();
    }

    // 日期格式化器（统一格式，避免解析问题）
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 调用 POST 接口发送告警信息
     * @param url 接口地址
     * @param dto 告警参数DTO
     * @return 接口响应（根据实际响应类型调整）
     * @throws IOException 处理文件时可能抛出的异常
     */
    public AjaxResult sendAlarm(String url, AlarmInitiateDTO dto) throws IOException {
        // 1. 构建 multipart/form-data 格式的参数
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();

        // 添加普通字段（转换为字符串）
        paramMap.add("algorithmType", dto.getAlgorithmType()); // 注意字段名与DTO一致（algorithm_ype）
        paramMap.add("alarmFlag", dto.getAlarmFlag().toString()); // 布尔值转字符串
        paramMap.add("startTime", DATE_FORMAT.format(dto.getBeginTime())); // 日期格式化
        paramMap.add("endTime", DATE_FORMAT.format(dto.getEndTime()));     // 日期格式化

        // 2. 处理文件（MultipartFile 转换为 ByteArrayResource）
        MultipartFile image = dto.getImage();
        if (image != null && !image.isEmpty()) {
            // 构建文件资源（需指定文件名，否则接收方可能无法获取原始文件名）
            ByteArrayResource imageResource = new ByteArrayResource(image.getBytes()) {
                @Override
                public String getFilename() {
                    return image.getOriginalFilename(); // 关键：设置原始文件名
                }
            };
            paramMap.add("image", imageResource); // 键名与DTO的image字段一致
        }

        // 3. 构建请求头（无需手动设置 Content-Type，RestTemplate 会自动处理为 multipart/form-data）
        HttpHeaders headers = new HttpHeaders();

        // 4. 构建请求实体
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(paramMap, headers);

        // 5. 发送 POST 请求（根据接口实际响应类型调整返回值，如 ResponseEntity<YourResponseDTO>）
        ResponseEntity<AjaxResult> response = new RestTemplate().postForEntity(url, requestEntity, AjaxResult.class);

        return response.getBody();
    }

    /**
     * 将本地文件转换为 MultipartFile 对象
     * @param filePath 本地文件绝对路径（例如：D:/images/test.jpg）
     * @return MultipartFile 对象
     * @throws IOException 当文件不存在或读取失败时抛出
     */
    public static MultipartFile convert(String filePath) throws IOException {
        // 1. 读取本地文件
        File localFile = new File(filePath);
        if (!localFile.exists()) {
            throw new IOException("本地文件不存在：" + filePath);
        }

        // 2. 构建文件输入流
        try (FileInputStream inputStream = new FileInputStream(localFile)) {
            // 3. 创建 MockMultipartFile 对象
            // 参数说明：
            // - name：表单字段名（与接口接收参数名一致，例如 "image"）
            // - originalFilename：原始文件名（例如 "test.jpg"）
            // - contentType：文件内容类型（例如 "image/jpeg"，未知可设为 null）
            // - inputStream：文件输入流
            return new MockMultipartFile(
                    "image",  // 对应接口接收的参数名（如 @RequestParam("image")）
                    localFile.getName(),  // 原始文件名
                    getContentType(localFile.getName()),  // 文件类型
                    inputStream  // 文件流
            );
        }
    }

    /**
     * 简单获取文件内容类型（根据文件名后缀）
     * 实际场景可使用更完善的工具类（如 Apache Tika）
     */
    private static String getContentType(String fileName) {
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else {
            return "application/octet-stream"; // 未知类型
        }
    }

}
