package com.maxinhai.platform.service;

import com.alibaba.fastjson2.JSONObject;
import com.maxinhai.platform.dto.AddStreamProxyDTO;
import com.maxinhai.platform.dto.DelStreamProxyDTO;
import com.maxinhai.platform.dto.ServiceConfigDTO;

public interface ZlmClient {

    JSONObject addFfmpegSource(ServiceConfigDTO param);

    JSONObject delFfmpegSource(ServiceConfigDTO param);

    JSONObject listFfmpegSource(ServiceConfigDTO param);

    /**
     * 添加rtsp/rtmp/hls拉流代理
     * @param param
     * @return
     */
    JSONObject addStreamProxy(AddStreamProxyDTO param);

    JSONObject delStreamProxy(DelStreamProxyDTO param);

    JSONObject listStreamProxy(ServiceConfigDTO param);

    JSONObject addStreamPusherProxy(ServiceConfigDTO param);

    JSONObject delStreamPusherProxy(ServiceConfigDTO param);

    JSONObject listStreamPusherProxy(ServiceConfigDTO param);

    JSONObject getProxyPusherInfo(ServiceConfigDTO param);

    JSONObject broadcastMessage(ServiceConfigDTO param);

    JSONObject openRtpServer(ServiceConfigDTO param);

    JSONObject openRtpServerMultiplex(ServiceConfigDTO param);

    JSONObject close_stream(ServiceConfigDTO param);

    JSONObject close_streams(ServiceConfigDTO param);

    JSONObject closeRtpServer(ServiceConfigDTO param);

    JSONObject connectRtpServer(ServiceConfigDTO param);

    JSONObject delete_webrtc(ServiceConfigDTO param);

    JSONObject deleteRecordDirectory(ServiceConfigDTO param);

    JSONObject downloadBin(ServiceConfigDTO param);

    JSONObject downloadFile(ServiceConfigDTO param);

    /**
     * 获取Session列表
     * @param param
     * @return
     */
    JSONObject getAllSession(ServiceConfigDTO param);

    /**
     * 获取服务器api列表
     * @param param
     * @return
     */
    JSONObject getApiList(ServiceConfigDTO param);

    JSONObject getMediaInfo(ServiceConfigDTO param);

    JSONObject getMediaList(ServiceConfigDTO param);

    JSONObject getMediaPlayerList(ServiceConfigDTO param);

    JSONObject getMP4RecordFile(ServiceConfigDTO param);

    JSONObject getProxyInfo(ServiceConfigDTO param);

    JSONObject getRtpInfo(ServiceConfigDTO param);

    JSONObject getServerConfig(ServiceConfigDTO param);

    JSONObject getSnap(ServiceConfigDTO param);

    /**
     * 获取主要对象个数
     * @param param
     * @return
     */
    JSONObject getStatistic(ServiceConfigDTO param);

    /**
     * 获取网络线程负载
     * @param param
     * @return
     */
    JSONObject getThreadsLoad(ServiceConfigDTO param);

    /**
     * 获取后台线程负载
     * @param param
     * @return
     */
    JSONObject getWorkThreadsLoad(ServiceConfigDTO param);

    JSONObject isMediaOnline(ServiceConfigDTO param);

    JSONObject isRecording(ServiceConfigDTO param);

    JSONObject kick_session(ServiceConfigDTO param);

    JSONObject kick_sessions(ServiceConfigDTO param);

    JSONObject listRtpSender(ServiceConfigDTO param);

    JSONObject listRtpServer(ServiceConfigDTO param);

    JSONObject loadMP4File(ServiceConfigDTO param);

    JSONObject pauseRtpCheck(ServiceConfigDTO param);

    JSONObject restartServer(ServiceConfigDTO param);

    JSONObject resumeRtpCheck(ServiceConfigDTO param);

    JSONObject seekRecordStamp(ServiceConfigDTO param);

    JSONObject setRecordSpeed(ServiceConfigDTO param);

    /**
     * 设置服务器配置
     * @param param
     * @return
     */
    JSONObject setServerConfig(ServiceConfigDTO param);

    JSONObject startRecord(ServiceConfigDTO param);

    JSONObject startSendRtp(ServiceConfigDTO param);

    JSONObject startSendRtpPassive(ServiceConfigDTO param);

    JSONObject startSendRtpTalk(ServiceConfigDTO param);

    JSONObject stopRecord(ServiceConfigDTO param);

    JSONObject stopSendRtp(ServiceConfigDTO param);

    JSONObject updateRtpServerSSRC(ServiceConfigDTO param);

    JSONObject version(ServiceConfigDTO param);

    JSONObject webrtc(ServiceConfigDTO param);

    JSONObject whep(ServiceConfigDTO param);

    JSONObject whip(ServiceConfigDTO param);

}
