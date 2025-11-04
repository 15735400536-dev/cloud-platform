package com.maxinhai.platform.dto;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

@Data
@Slf4j
@ApiModel(description = "告警发起DTO")
public class AlarmInitiateDTO {

    /**
     * 算法类型
     */
    @ApiModelProperty("算法类型：person.人员识别 hat.安全帽 fire.火灾")
    private String algorithmType;

    /**
     * 告警标记：true.发起 false.消除
     */
    @ApiModelProperty("告警标记：true.发起 false.消除")
    private Boolean alarmFlag;

    /**
     * 告警图片
     */
    @ApiModelProperty("告警图片")
    private MultipartFile image;

    @ApiModelProperty("告警开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    @ApiModelProperty("告警结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    public Date getBeginTime() {
        return Objects.isNull(this.beginTime) ? new Date() : this.beginTime;
    }

    public Date getEndTime() {
        return Objects.isNull(this.endTime) ? new Date() : this.endTime;
    }

    /**
     * 构建JsonObject
     *
     * @param dto 告警信息
     * @return
     */
    public static JSONObject buildJsonObject(AlarmInitiateDTO dto) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("algorithmType", dto.getAlgorithmType());
        jsonObject.set("alarmFlag", dto.getAlarmFlag());
        try {
            jsonObject.set("alarmImage", getImageStr(dto.getImage()));
        } catch (IOException e) {
            log.error("上传图片转换出错：{}", e.getMessage());
        }
        jsonObject.set("beginTime", dto.getBeginTime());
        jsonObject.set("endTime", dto.getEndTime());
        return jsonObject;
    }

    /**
     * 上传文件转Base64
     *
     * @param file 上传文件
     * @return
     * @throws IOException
     */
    public static String fileToBase64(MultipartFile file) throws IOException {
        return Base64.encode(file.getBytes());
    }

    /**
     * 获取文件名称后缀
     *
     * @param file 上传文件
     * @return
     */
    public static String getFileSuffix(MultipartFile file) {
        return FileUtil.getSuffix(file.getOriginalFilename());
    }

    /**
     * 上传文件转化为自定义字符串
     *
     * @param file 上传文件
     * @return
     * @throws IOException
     */
    public static String getImageStr(MultipartFile file) throws IOException {
        return getFileSuffix(file) + "," + fileToBase64(file);
    }

    /**
     * 生成文件名称
     *
     * @param jsonObject 告警信息
     * @return
     */
    public static String getImageName(JSONObject jsonObject) {
        String algorithmType = jsonObject.getStr("algorithmType");
        String fileSuffix = jsonObject.getStr("alarmImage").split(",")[0];
        return algorithmType + "_" +
                DateUtil.format(new Date(), "yyyy_MM_dd_HH_mm_ss") +
                "." + fileSuffix;
    }

    /**
     * 将告警图片保存到指定路径
     *
     * @param jsonObject 告警信息
     * @param filePath   文件路径
     */
    public static void writeToFile(JSONObject jsonObject, String filePath) {
        String base64 = jsonObject.getStr("alarmImage").split(",")[1];
        byte[] bytes = Base64.decode(base64);
        FileUtil.writeBytes(bytes, filePath);
    }

}
