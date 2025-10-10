package com.maxinhai.platform.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/index/hook")
@Api(tags = "WebHook管理接口")
public class WebHookController {

    @PostMapping("/on_play")
    public void on_play(@RequestBody Map<String, Object> params) {
        log.info("on_play: {}", params);
    }

    @PostMapping("/on_publish")
    public void on_publish(@RequestBody Map<String, Object> params) {
        log.info("on_publish: {}", params);
    }

    @PostMapping("/on_record_mp4")
    public void on_record_mp4(@RequestBody Map<String, Object> params) {
        log.info("on_record_mp4: {}", params);
    }

    @PostMapping("/on_record_ts")
    public void on_record_ts(@RequestBody Map<String, Object> params) {
        log.info("on_record_ts: {}", params);
    }

    @PostMapping("/on_rtp_server_timeout")
    public void on_rtp_server_timeout(@RequestBody Map<String, Object> params) {
        log.info("on_rtp_server_timeout: {}", params);
    }

    @PostMapping("/on_rtsp_auth")
    public void on_rtsp_auth(@RequestBody Map<String, Object> params) {
        log.info("on_rtsp_auth: {}", params);
    }

    @PostMapping("/on_send_rtp_stopped")
    public void on_send_rtp_stopped(@RequestBody Map<String, Object> params) {
        log.info("on_send_rtp_stopped: {}", params);
    }

    @PostMapping("/on_server_exited")
    public void on_server_exited(@RequestBody Map<String, Object> params) {
        log.info("on_server_exited: {}", params);
    }

    @PostMapping("/on_server_keepalive")
    public void on_server_keepalive(@RequestBody Map<String, Object> params) {
        log.info("on_server_keepalive: {}", params);
    }

    @PostMapping("/on_server_started")
    public void on_server_started(@RequestBody Map<String, Object> params) {
        log.info("on_server_started: {}", params);
    }

    @PostMapping("/on_stream_changed")
    public void on_stream_changed(@RequestBody Map<String, Object> params) {
        log.info("on_stream_changed: {}", params);
    }

    @PostMapping("/on_stream_none_reader")
    public void on_stream_none_reader(@RequestBody Map<String, Object> params) {
        log.info("on_stream_none_reader: {}", params);
    }

    @PostMapping("/on_stream_not_found")
    public void on_stream_not_found(@RequestBody Map<String, Object> params) {
        log.info("on_stream_not_found: {}", params);
    }

    @PostMapping("/on_http_access")
    public void on_http_access(@RequestBody Map<String, Object> params) {
        log.info("on_http_access: {}", params);
    }


}
