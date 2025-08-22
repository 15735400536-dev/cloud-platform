package com.maxinhai.platform.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.maxinhai.platform.dto.AddStreamProxyDTO;
import com.maxinhai.platform.dto.DelStreamProxyDTO;
import com.maxinhai.platform.dto.ServiceConfigDTO;
import com.maxinhai.platform.service.ZlmClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ZlmClientImpl implements ZlmClient {

    @Resource
    private RestTemplate restTemplate;

    @Override
    public JSONObject addFfmpegSource(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/addFfmpegSource?secret=%s";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> request = new HashMap<>();
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(request, headers);
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), httpEntity, JSONObject.class);
    }

    @Override
    public JSONObject delFfmpegSource(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/delFfmpegSource?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject listFfmpegSource(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/listFfmpegSource?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject addStreamProxy(AddStreamProxyDTO param) {
        String url = "http://%s:%d/index/api/addStreamProxy";
        // 创建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 创建请求体
        Map<String, Object> request = new HashMap<>();
        request.put("secret", param.getSecret());
        request.put("vhost", param.getVhost());
        request.put("app", param.getApp());
        request.put("stream", param.getStream());
        request.put("url", param.getUrl());
        // 封装请求头和请求体
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(request, headers);
        return restTemplate.postForEntity(String.format(url, param.getIp(), param.getPort()), httpEntity, JSONObject.class).getBody();
    }

    @Override
    public JSONObject delStreamProxy(DelStreamProxyDTO param) {
        String url = "http://%s:%d/index/api/delStreamProxy";
        // 创建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 创建请求体
        Map<String, Object> request = new HashMap<>();
        request.put("secret", param.getSecret());
        request.put("key", param.getKey());
        // 封装请求头和请求体
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(request, headers);
        return restTemplate.postForEntity(String.format(url, param.getIp(), param.getPort()), httpEntity, JSONObject.class).getBody();
    }

    @Override
    public JSONObject listStreamProxy(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/listStreamProxy?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject addStreamPusherProxy(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/addStreamPusherProxy?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject delStreamPusherProxy(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/delStreamPusherProxy?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject listStreamPusherProxy(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/listStreamPusherProxy?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject getProxyPusherInfo(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/getProxyPusherInfo?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject broadcastMessage(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/broadcastMessage?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject openRtpServer(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/openRtpServer?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject openRtpServerMultiplex(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/openRtpServerMultiplex?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject close_stream(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/close_stream?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject close_streams(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/close_streams?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject closeRtpServer(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/closeRtpServer?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject connectRtpServer(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/connectRtpServer?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject delete_webrtc(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/delete_webrtc?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject deleteRecordDirectory(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/deleteRecordDirectory?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject downloadBin(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/downloadBin?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject downloadFile(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/downloadFile?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject getAllSession(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/getAllSession?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject getApiList(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/getApiList?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject getMediaInfo(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/getMediaInfo?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject getMediaList(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/getMediaList?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject getMediaPlayerList(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/getMediaPlayerList?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject getMP4RecordFile(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/getMP4RecordFile?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject getProxyInfo(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/getProxyInfo?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject getRtpInfo(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/getRtpInfo?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject getServerConfig(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/getServerConfig?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject getSnap(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/getSnap?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject getStatistic(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/getStatistic?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject getThreadsLoad(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/getThreadsLoad?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject getWorkThreadsLoad(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/getWorkThreadsLoad?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject isMediaOnline(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/isMediaOnline?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject isRecording(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/isRecording?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject kick_session(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/kick_session?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject kick_sessions(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/kick_sessions?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject listRtpSender(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/listRtpSender?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject listRtpServer(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/listRtpServer?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject loadMP4File(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/loadMP4File?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject pauseRtpCheck(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/pauseRtpCheck?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject restartServer(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/restartServer?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject resumeRtpCheck(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/resumeRtpCheck?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject seekRecordStamp(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/seekRecordStamp?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject setRecordSpeed(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/setRecordSpeed?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject setServerConfig(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/setServerConfig";
        // 创建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 创建请求体
        Map<String, Object> request = new HashMap<>();
        request.put("secret", param.getSecret());
        request.put("hook.enable", "1");
        request.put("api.secret", param.getSecret());
        request.put("hook.on_play", String.format("http://%s:%d/index/hook/on_play", "192.168.1.18", 10070));
        request.put("hook.on_publish", String.format("http://%s:%d/index/hook/on_publish", "192.168.1.18", 10070));
        request.put("hook.on_record_mp4", String.format("http://%s:%d/index/hook/on_record_mp4", "192.168.1.18", 10070));
        request.put("hook.on_record_ts", String.format("http://%s:%d/index/hook/on_record_ts", "192.168.1.18", 10070));
        request.put("hook.on_rtp_server_timeout", String.format("http://%s:%d/index/hook/on_rtp_server_timeout", "192.168.1.18", 10070));
        request.put("hook.on_rtsp_auth", String.format("http://%s:%d/on_rtsp_auth", "192.168.1.18", 10070));
        request.put("hook.on_send_rtp_stopped", String.format("http://%s:%d/index/hook/on_send_rtp_stopped", "192.168.1.18", 10070));
        request.put("hook.on_server_exited", String.format("http://%s:%d/index/hook/on_server_exited", "192.168.1.18", 10070));
        request.put("hook.on_server_keepalive", String.format("http://%s:%d/index/hook/on_server_keepalive", "192.168.1.18", 10070));
        request.put("hook.on_server_started", String.format("http://%s:%d/index/hook/on_server_started", "192.168.1.18", 10070));
        request.put("hook.on_stream_changed", String.format("http://%s:%d/index/hook/on_stream_changed", "192.168.1.18", 10070));
        request.put("hook.on_stream_none_reader", String.format("http://%s:%d/index/hook/on_stream_none_reader", "192.168.1.18", 10070));
        request.put("hook.on_stream_not_found", String.format("http://%s:%d/index/hook/on_stream_not_found", "192.168.1.18", 10070));
        request.put("hook.on_http_access", "");

        // 封装请求头和请求体
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(request, headers);
        return restTemplate.postForEntity(String.format(url, param.getIp(), param.getPort()), httpEntity, JSONObject.class).getBody();
    }

    @Override
    public JSONObject startRecord(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/startRecord?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject startSendRtp(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/startSendRtp?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject startSendRtpPassive(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/startSendRtpPassive?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject startSendRtpTalk(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/startSendRtpTalk?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject stopRecord(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/stopRecord?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject stopSendRtp(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/stopSendRtp?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject updateRtpServerSSRC(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/updateRtpServerSSRC?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject version(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/version?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject webrtc(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/webrtc?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject whep(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/whep?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }

    @Override
    public JSONObject whip(ServiceConfigDTO param) {
        String url = "http://%s:%d/index/api/whip?secret=%s";
        return restTemplate.postForObject(String.format(url, param.getIp(), param.getPort(), param.getSecret()), param, JSONObject.class);
    }
}
