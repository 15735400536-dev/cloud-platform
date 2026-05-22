package com.maxinhai.platform.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import com.maxinhai.platform.handler.ListHandler;
import com.maxinhai.platform.handler.StringHandler;
import com.maxinhai.platform.mapper.UserMapper;
import com.maxinhai.platform.po.User;
import com.maxinhai.platform.service.SystemInfoService;
import com.maxinhai.platform.utils.ClientInfoUtils;
import com.maxinhai.platform.utils.ServerInfoUtils;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SystemInfoServiceImpl implements SystemInfoService {

    @Resource
    private StringHandler stringHandler;
    @Resource
    private ListHandler listHandler;
    @Resource
    private UserMapper userMapper;
    @Resource
    @Qualifier(value = "ioIntensiveExecutor")
    private Executor ioIntensiveExecutor;
    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    @XxlJob("updateSystemInfo")
    public void updateSystemInfo() {
        Map<String, Object> serverInfo = ServerInfoUtils.getServerInfo();
        stringHandler.set("serverInfo", serverInfo);
        XxlJobHelper.log("更新系统信息完成");
    }

    @XxlJob("getFollowMap")
    public void getFollowMap() {
        Long size = listHandler.size("list:user");
        List<Object> objectList = listHandler.range("list:user", 0, size - 1);
        Set<String> collect = null;
        try {
            collect = objectList.stream()
                    .map(obj -> JSONUtil.toBean(obj.toString(), User.class))
                    .map(User::getUsername)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            XxlJobHelper.log("发生错误:" + e.getMessage());
        }

        Map<String, String> headers = new HashMap<>();
        //String cookie = "enter_pc_once=1; UIFID_TEMP=28ea90c1b0cf804752225259882c701fb12323f08ef828fc1032b615e29efbbe75787543492bebd257f5296f135af5e95a85ef70f4d644b7cd5fcaf50b3c252d30726ed888fc72d08b9b8005a602c20b14958713b76580013a1ea677a38cdafd0bf33bb0119d613755293ac37373a4a1; hevc_supported=true; bd_ticket_guard_client_web_domain=2; fpk1=U2FsdGVkX19LdFCDnsvlepUyu1pUN52B4AfpIu7EU9C7GSadbsYJs+sfSM+82xufF0aGJVCX81J45zieeNQ46Q==; fpk2=0e0369e2813db7deb26e5937c353aab4; UIFID=28ea90c1b0cf804752225259882c701fb12323f08ef828fc1032b615e29efbbe75787543492bebd257f5296f135af5e95a85ef70f4d644b7cd5fcaf50b3c252dca6f93450bcfa0e23994c681fa0cd844a2ac27e1af9c20f72360c1928b32f6a05d09138b25ebee9e7b8392f9955f3a89ecf1cd0ea8a784cdbc7e53508cf1447216873ecdbb1f151b4be58dc7b8c57eb0e1c410201be33fae0cc1ab3adcf48fefb95beef3206de3103ae9ca85eeb300b040b098d1c8d60dc7ad92be6ee00026bf; xgplayer_device_id=99191440500; xgplayer_user_id=588518695396; d_ticket=9d14ab04433a5178efa52a9d662a348e2abb8; passport_assist_user=CkHTHU6J5iymqmuH3dFJnHHRXRKdChyQQWMg5-lKDjBtftFSuRfoPhVjTiw5T9Wh_18sE72F2CU47Hcs1bissLioARpKCjwAAAAAAAAAAAAAT871_uK2cQDPz3PoRDcfaSeCfq9kEFVKPJr1pOuVC6yOVr8d_E7Vnf-TqZi9nMgjFdgQv9ODDhiJr9ZUIAEiAQOtobET; n_mh=imZMMIWwPRng1tRBBpq52CaKAFQRTJGogxg7dP1uyAA; uid_tt=29042a4e756b6eaf5ef445929cf65519; uid_tt_ss=29042a4e756b6eaf5ef445929cf65519; sid_tt=3adc60874a98e5c918a022811fc67430; sessionid=3adc60874a98e5c918a022811fc67430; sessionid_ss=3adc60874a98e5c918a022811fc67430; is_staff_user=false; login_time=1765206150747; live_use_vvc=%22false%22; theme=%22dark%22; manual_theme=%22dark%22; __live_version__=%221.1.4.6396%22; my_rd=2; PhoneResumeUidCacheV1=%7B%222283008886053965%22%3A%7B%22time%22%3A1767428232466%2C%22noClick%22%3A3%7D%7D; s_v_web_id=verify_mm6i62jy_PULp6yju_ibeo_4NDm_9l2a_aaWMgaLactr2; douyin.com; xg_device_score=7.951531735915437; device_web_cpu_core=20; device_web_memory_size=8; architecture=amd64; dy_swidth=1920; dy_sheight=1080; is_dash_user=1; passport_csrf_token=d2ec8113c6df622d2295b34e94a9d186; passport_csrf_token_default=d2ec8113c6df622d2295b34e94a9d186; strategyABtestKey=%221772294872.964%22; ttwid=1%7CmG79TlZI2xO1KhJb3jG3x26-gLcjgyub1Fq-aLx1aSU%7C1772294931%7C4d1c2ad918bb7f30d4079874e2efcf3731a73b1327ddf22b44f984a847356985; playRecommendGuideTagCount=7; totalRecommendGuideTagCount=7; download_guide=%222%2F20260301%2F0%22; __ac_signature=_02B4Z6wo00f01rCzaZQAAIDDb-ia20aoQ0awk20AAMWb4d; passport_mfa_token=Cjewi%2FofXVGJ%2FU8IS3AgnCF%2FvF7Y%2B1DCQDX0XfiZ2486iYsD%2BtKWqNQolRccvP9nwELWQF9p3d05GkoKPAAAAAAAAAAAAABQISx08wWmYnusxS%2BY8Tsl41rvMhaue7Yf3hdNYDm3TKVs2A4h%2B%2BTlPUL%2Bud2QXycaMRDm84oOGPax0WwgAiIBA%2BLW0yY%3D; sid_guard=3adc60874a98e5c918a022811fc67430%7C1772335555%7C5142651%7CWed%2C+29-Apr-2026+15%3A56%3A46+GMT; session_tlb_tag=sttt%7C9%7COtxgh0qY5ckYoCKBH8Z0MP________-eKj9kPG2Fd2AwEOT1qTenOEqhN9XwtpI4PYsHk0sEuKI%3D; sid_ucp_v1=1.0.0-KDY3YWNiYjBiNjkxMmVmMTRhN2M4ZGYyMzY5NzQ0MjBiNTAwZDRkMzEKIQjNuLDtpoyHBBDD447NBhjvMSAMMLC1m5kGOAJA8QdIBBoCbHEiIDNhZGM2MDg3NGE5OGU1YzkxOGEwMjI4MTFmYzY3NDMw; ssid_ucp_v1=1.0.0-KDY3YWNiYjBiNjkxMmVmMTRhN2M4ZGYyMzY5NzQ0MjBiNTAwZDRkMzEKIQjNuLDtpoyHBBDD447NBhjvMSAMMLC1m5kGOAJA8QdIBBoCbHEiIDNhZGM2MDg3NGE5OGU1YzkxOGEwMjI4MTFmYzY3NDMw; _bd_ticket_crypt_doamin=2; _bd_ticket_crypt_cookie=9c06b987b7c59bd066f884bb960e2857; __security_server_data_status=1; SelfTabRedDotControl=%5B%7B%22id%22%3A%227598463143320225802%22%2C%22u%22%3A67%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227585393259948574739%22%2C%22u%22%3A149%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227606338889006991411%22%2C%22u%22%3A4%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227579940749522536494%22%2C%22u%22%3A100%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227434002140684634139%22%2C%22u%22%3A66%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227455173661570369574%22%2C%22u%22%3A179%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227513561970461640714%22%2C%22u%22%3A213%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227595887514071386154%22%2C%22u%22%3A90%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227255621815098148900%22%2C%22u%22%3A102%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227566644882363418650%22%2C%22u%22%3A36%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%226800717572464461832%22%2C%22u%22%3A484%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227597433500878243886%22%2C%22u%22%3A40%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227585437717849180201%22%2C%22u%22%3A23%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227572813243421296667%22%2C%22u%22%3A281%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227579976572292417578%22%2C%22u%22%3A151%2C%22c%22%3A0%7D%5D; volume_info=%7B%22isMute%22%3Afalse%2C%22volume%22%3A0.445%2C%22isUserMute%22%3Afalse%7D; stream_recommend_feed_params=%22%7B%5C%22cookie_enabled%5C%22%3Atrue%2C%5C%22screen_width%5C%22%3A1920%2C%5C%22screen_height%5C%22%3A1080%2C%5C%22browser_online%5C%22%3Atrue%2C%5C%22cpu_core_num%5C%22%3A20%2C%5C%22device_memory%5C%22%3A8%2C%5C%22downlink%5C%22%3A10%2C%5C%22effective_type%5C%22%3A%5C%224g%5C%22%2C%5C%22round_trip_time%5C%22%3A50%7D%22; FOLLOW_LIVE_POINT_INFO=%22MS4wLjABAAAAMutji20RI-BreK3_03pd35nDj5AhhEaaLPg7Bh5efyuTIOcDYIVUilE5nBrtifz5%2F1772380800000%2F0%2F0%2F1772337415088%22; FOLLOW_NUMBER_YELLOW_POINT_INFO=%22MS4wLjABAAAAMutji20RI-BreK3_03pd35nDj5AhhEaaLPg7Bh5efyuTIOcDYIVUilE5nBrtifz5%2F1772380800000%2F0%2F0%2F1772338015089%22; publish_badge_show_info=%220%2C0%2C0%2C1772336841343%22; odin_tt=9cae4bfa56fbc9eb02518b7cd7d9d8f26a5180ccd738598e655d182e82597179e10fee6d6a46aa7b4089d31eddf96dcc449381b66c02232f0f8e9ead9284ae92; bd_ticket_guard_client_data=eyJiZC10aWNrZXQtZ3VhcmQtdmVyc2lvbiI6MiwiYmQtdGlja2V0LWd1YXJkLWl0ZXJhdGlvbi12ZXJzaW9uIjoxLCJiZC10aWNrZXQtZ3VhcmQtcmVlLXB1YmxpYy1rZXkiOiJCS0tSd2hpcll3VXExU0JnVTlGTEQ3T2tiNXRkQXpmQkRCVVk1aUs2OHJPaXZPUlVJNEgvcmFTekhRRmw3SnArOGJqY0tqWXNDRzBOUi83OWlSSklnL3M9IiwiYmQtdGlja2V0LWd1YXJkLXdlYi12ZXJzaW9uIjoyfQ%3D%3D; bd_ticket_guard_client_data_v2=eyJyZWVfcHVibGljX2tleSI6IkJLS1J3aGlyWXdVcTFTQmdVOUZMRDdPa2I1dGRBemZCREJVWTVpSzY4ck9pdk9SVUk0SC9yYVN6SFFGbDdKcCs4YmpjS2pZc0NHME5SLzc5aVJKSWcvcz0iLCJ0c19zaWduIjoidHMuMi5iZjMyYzhkMjQyZGQwYWU0NGQyNWU1NzQwY2Q5NWViYjg2MzY3MTUwOWM1ZTBlNjBhNGQ3NDY3ZTVkOTkyZmQ4YzRmYmU4N2QyMzE5Y2YwNTMxODYyNGNlZGExNDkxMWNhNDA2ZGVkYmViZWRkYjJlMzBmY2U4ZDRmYTAyNTc1ZCIsInJlcV9jb250ZW50Ijoic2VjX3RzIiwicmVxX3NpZ24iOiJoSDNOaStrVVpCQXVMaHZxREUzamo4aEIxL05QUWNvTzI2L3l2ZGFic0hZPSIsInNlY190cyI6IiNUVTRpTTZJRTBkUVE2MWZ4ZWdVWWFRRUZrem1rdE5uZmVZZi9obGl6aGRmREhSYlhmRkx5ejlJOEw2U3IifQ%3D%3D; home_can_add_dy_2_desktop=%221%22; __security_mc_1_s_sdk_crypt_sdk=f09e3767-469c-8be6; __security_mc_1_s_sdk_cert_key=9481cb33-450d-a84f; __security_mc_1_s_sdk_sign_data_key_web_protect=67eef6af-4168-95be; biz_trace_id=d2249e74; stream_player_status_params=%22%7B%5C%22is_auto_play%5C%22%3A0%2C%5C%22is_full_screen%5C%22%3A0%2C%5C%22is_full_webscreen%5C%22%3A1%2C%5C%22is_mute%5C%22%3A0%2C%5C%22is_speed%5C%22%3A1%2C%5C%22is_visible%5C%22%3A1%7D%22; IsDouyinActive=true; __ac_nonce=069a3bafa008f4fa4b25b";
        String cookie = "enter_pc_once=1; UIFID_TEMP=28ea90c1b0cf804752225259882c701fb12323f08ef828fc1032b615e29efbbe75787543492bebd257f5296f135af5e95a85ef70f4d644b7cd5fcaf50b3c252d30726ed888fc72d08b9b8005a602c20b14958713b76580013a1ea677a38cdafd0bf33bb0119d613755293ac37373a4a1; hevc_supported=true; bd_ticket_guard_client_web_domain=2; fpk1=U2FsdGVkX19LdFCDnsvlepUyu1pUN52B4AfpIu7EU9C7GSadbsYJs+sfSM+82xufF0aGJVCX81J45zieeNQ46Q==; fpk2=0e0369e2813db7deb26e5937c353aab4; UIFID=28ea90c1b0cf804752225259882c701fb12323f08ef828fc1032b615e29efbbe75787543492bebd257f5296f135af5e95a85ef70f4d644b7cd5fcaf50b3c252dca6f93450bcfa0e23994c681fa0cd844a2ac27e1af9c20f72360c1928b32f6a05d09138b25ebee9e7b8392f9955f3a89ecf1cd0ea8a784cdbc7e53508cf1447216873ecdbb1f151b4be58dc7b8c57eb0e1c410201be33fae0cc1ab3adcf48fefb95beef3206de3103ae9ca85eeb300b040b098d1c8d60dc7ad92be6ee00026bf; xgplayer_device_id=99191440500; xgplayer_user_id=588518695396; d_ticket=9d14ab04433a5178efa52a9d662a348e2abb8; is_staff_user=false; live_use_vvc=%22false%22; theme=%22dark%22; manual_theme=%22dark%22; my_rd=2; __security_server_data_status=1; MONITOR_WEB_ID=91997eb8-833e-4dc9-8227-54daef0fe930; dy_swidth=1920; dy_sheight=1080; is_dash_user=1; live_private_user=0; SEARCH_RESULT_LIST_TYPE=%22single%22; use_biz_token=true; has_biz_token=false; download_guide=%223%2F20260403%2F1%22; LivePausePop=%22%257B%2522todayCount%2522%253A1%252C%2522closeNum%2522%253A2%252C%2522todayShowRoom%2522%253A%25227631911633249536795%2522%252C%2522lastTimer%2522%253A1776946971588%257D%22; __live_version__=%221.1.5.1418%22; s_v_web_id=verify_molg9g2q_0cIju2Q0_1b8T_4ef1_9AUi_7QKy3Gnb6hJv; passport_csrf_token=f1eaa97baeabd423d808ceb4852e46d2; passport_csrf_token_default=f1eaa97baeabd423d808ceb4852e46d2; __druidClientInfo=JTdCJTIyY2xpZW50V2lkdGglMjIlM0E0ODMlMkMlMjJjbGllbnRIZWlnaHQlMjIlM0E4MjMlMkMlMjJ3aWR0aCUyMiUzQTQ4MyUyQyUyMmhlaWdodCUyMiUzQTgyMyUyQyUyMmRldmljZVBpeGVsUmF0aW8lMjIlM0ExJTJDJTIydXNlckFnZW50JTIyJTNBJTIyTW96aWxsYSUyRjUuMCUyMChXaW5kb3dzJTIwTlQlMjAxMC4wJTNCJTIwV2luNjQlM0IlMjB4NjQpJTIwQXBwbGVXZWJLaXQlMkY1MzcuMzYlMjAoS0hUTUwlMkMlMjBsaWtlJTIwR2Vja28pJTIwQ2hyb21lJTJGMTM2LjAuMC4wJTIwU2FmYXJpJTJGNTM3LjM2JTIyJTdE; vdg_s=1; douyin.com; xg_device_score=7.9191272394219645; device_web_cpu_core=20; device_web_memory_size=8; architecture=amd64; is_support_rtm_web_ts=1; PhoneResumeUidCacheV1=%7B%222283008886053965%22%3A%7B%22time%22%3A1777701007094%2C%22noClick%22%3A3%7D%2C%223182428629500586%22%3A%7B%22time%22%3A1776940852345%2C%22noClick%22%3A1%7D%7D; publish_badge_show_info=%221%2C0%2C0%2C1777731459114%22; strategyABtestKey=%221777784784.071%22; stream_recommend_feed_params=%22%7B%5C%22cookie_enabled%5C%22%3Atrue%2C%5C%22screen_width%5C%22%3A1920%2C%5C%22screen_height%5C%22%3A1080%2C%5C%22browser_online%5C%22%3Atrue%2C%5C%22cpu_core_num%5C%22%3A20%2C%5C%22device_memory%5C%22%3A8%2C%5C%22downlink%5C%22%3A10%2C%5C%22effective_type%5C%22%3A%5C%224g%5C%22%2C%5C%22round_trip_time%5C%22%3A50%7D%22; shareRecommendGuideTagNegative=1; shareRecommendGuideTagCount=0; __ac_signature=_02B4Z6wo00f01jPVjcwAAIDD7I5-gAhirioz9YlAAOUR22; stream_player_status_params=%22%7B%5C%22is_auto_play%5C%22%3A0%2C%5C%22is_full_screen%5C%22%3A0%2C%5C%22is_full_webscreen%5C%22%3A0%2C%5C%22is_mute%5C%22%3A0%2C%5C%22is_speed%5C%22%3A1%2C%5C%22is_visible%5C%22%3A1%7D%22; gulu_source_res=eyJwX2luIjoiMWNiYzdlMGU3OTFhOTYxMzEyMWJjY2MwOTBiMTgxNzdiNzk5N2Q1MmU1YThhZWZjZDQ0NDI2ZDM5ODZkNmUxZCJ9; playRecommendGuideTagCount=4; totalRecommendGuideTagNegative=1; totalRecommendGuideTagCount=0; __security_mc_1_s_sdk_crypt_sdk=a3750f7d-449a-bcdf; __security_mc_1_s_sdk_cert_key=8580ea75-4474-93e6; volume_info=%7B%22isMute%22%3Afalse%2C%22isUserMute%22%3Afalse%2C%22volume%22%3A0.401%7D; sdk_source_info=7e276470716a68645a606960273f276364697660272927676c715a6d6069756077273f276364697660272927666d776a68605a607d71606b766c6a6b5a7666776c7571273f275e58272927666a6b766a69605a696c6061273f27636469766027292762696a6764695a7364776c6467696076273f275e582729277672715a646971273f2763646976602729277f6b5a666475273f2763646976602729276d6a6e5a6b6a716c273f2763646976602729276c6b6f5a7f6367273f27636469766027292771273f27313132353d33313c3232323234272927676c715a75776a716a666a69273f2763646976602778; bit_env=-7xxZJeG4K_ooTYlvuApWBDxbQW2LFOWARKEkCVDZEwjjyk2r7S3PNkXSTnGWb6x2OmoyLoW0ZLC_61DIFV5VB8XjsafaiKKDlDZzMUPXEXl6xhr2WTHWfip3mVniBf_qjXFjfZp1uNHHiToZgaUqvFKA91RZO5boZTOxc4HOSwdCFa8TJXDphzR2KfFrNUGRrOEV26DY2Vo5MiEyNxeh5FNLPpAk97049cYIyVH8nnDNR4snB7kr7Dn2w9BcN7IDLmsWSrxSyGr13K2jhMlHb59Gnk5zLRR3RkVdYQiZ4kMxj7iE2LWy1UZgvflQDTobMrIAI9HilqQsHttChm2pJRPYdSyXaQ2FJsBhrgduK-MB3fPJc2UyjTcFZ7qmtO4KMZn2oRyfUVihTawmFVfmnXw2hYJSxpl0t8gt_BzOEiksA1l0k24Hy0EUfr2M13DVdOB3WGL4VBM0z-cxJruXf6llS6I4JnGnFvdWm30114GlkpDIAK1zSyfvtNGzbhl; passport_auth_mix_state=likm09wof2wvck9m43ik3y0s619vsnvf; passport_assist_user=CkFOVylfZCzVuscs0THYQqJtbeD8i9mciWMX-Sgub4a22_ak1HdjFzr902vk-iDqYjDvl4laFGZgWve5fuDteBgl9BpKCjwAAAAAAAAAAAAAUGBPgHjoMGfI2utmxFI3Pvxn7jk3uKs3dnN2VvqzMAk-Q7wh3HxRlBgLZ7pFUiuZwIAQnbuQDhiJr9ZUIAEiAQOgDIy1; n_mh=JK68gn5Ha3GPWFmpO_oii_x9Q_nvd4_jr2MwT9ETKB8; sid_guard=14b4ff809ac73c4575b8e03b71269c2d%7C1777794728%7C5184000%7CThu%2C+02-Jul-2026+07%3A52%3A08+GMT; uid_tt=a620e0aa28559f6c40e27febfbc05227; uid_tt_ss=a620e0aa28559f6c40e27febfbc05227; sid_tt=14b4ff809ac73c4575b8e03b71269c2d; sessionid=14b4ff809ac73c4575b8e03b71269c2d; sessionid_ss=14b4ff809ac73c4575b8e03b71269c2d; session_tlb_tag=sttt%7C5%7CFLT_gJrHPEV1uOA7cSacLf_________K-KnoI8973NhFSk9GEIrq5mLAixPAaIIqZkHni4MPnJM%3D; sid_ucp_v1=1.0.0-KDcyNjk0MmVhZjc5MTE0ZjJjM2ExYWJmYzI0NjkyN2Q2YmQwNzczZjEKIQiqhfC_7szTBRCo_dvPBhjvMSAMMOD-ubsGOAVA-wdIBBoCbHEiIDE0YjRmZjgwOWFjNzNjNDU3NWI4ZTAzYjcxMjY5YzJk; ssid_ucp_v1=1.0.0-KDcyNjk0MmVhZjc5MTE0ZjJjM2ExYWJmYzI0NjkyN2Q2YmQwNzczZjEKIQiqhfC_7szTBRCo_dvPBhjvMSAMMOD-ubsGOAVA-wdIBBoCbHEiIDE0YjRmZjgwOWFjNzNjNDU3NWI4ZTAzYjcxMjY5YzJk; __security_mc_1_s_sdk_sign_data_key_web_protect=4d913117-4322-8d75; login_time=1777794728792; _bd_ticket_crypt_cookie=be9a824cc9cf97a4449c2533a6e05e8a; DiscoverFeedExposedAd=%7B%7D; IsDouyinActive=true; SelfTabRedDotControl=%5B%7B%22id%22%3A%227567605148063631412%22%2C%22u%22%3A201%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227240069862343575612%22%2C%22u%22%3A161%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227589864947652397094%22%2C%22u%22%3A53%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227587949830454904832%22%2C%22u%22%3A351%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227426730449805772811%22%2C%22u%22%3A35%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227345400137998403621%22%2C%22u%22%3A317%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227616318298998245376%22%2C%22u%22%3A159%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227620269492832045094%22%2C%22u%22%3A148%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227625267095022635054%22%2C%22u%22%3A8%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227579619178613573658%22%2C%22u%22%3A44%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227584026699601053747%22%2C%22u%22%3A130%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227520867783370868746%22%2C%22u%22%3A144%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227634927658132604928%22%2C%22u%22%3A11%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227608155681069205504%22%2C%22u%22%3A11%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227607332791604217907%22%2C%22u%22%3A48%2C%22c%22%3A0%7D%5D; FOLLOW_NUMBER_YELLOW_POINT_INFO=%22MS4wLjABAAAACZf9q5GoAVE5eR4wX1nbLRILQSEwfkHqEOmC-i0wmm6lzx_wnu6-72LJU3-0tN6L%2F1777824000000%2F0%2F1777794730318%2F0%22; bd_ticket_guard_client_data=eyJiZC10aWNrZXQtZ3VhcmQtdmVyc2lvbiI6MiwiYmQtdGlja2V0LWd1YXJkLWl0ZXJhdGlvbi12ZXJzaW9uIjoxLCJiZC10aWNrZXQtZ3VhcmQtcmVlLXB1YmxpYy1rZXkiOiJCS0tSd2hpcll3VXExU0JnVTlGTEQ3T2tiNXRkQXpmQkRCVVk1aUs2OHJPaXZPUlVJNEgvcmFTekhRRmw3SnArOGJqY0tqWXNDRzBOUi83OWlSSklnL3M9IiwiYmQtdGlja2V0LWd1YXJkLXdlYi12ZXJzaW9uIjoyfQ%3D%3D; ttwid=1%7CmG79TlZI2xO1KhJb3jG3x26-gLcjgyub1Fq-aLx1aSU%7C1777794730%7C0bde54bc451d69fbd07fe48e23222df82c9deddb823cfa530d52dbb05947006b; odin_tt=82b2255b3ab7fcaac39e5aa1ef3bcdff941b157e6f1695649ebaa17f1168da7fa466b0fac2cff99ab4988fc942470e3f3b17aeeeb99b7dec8e2738a548600d29418fd99b45d5cc38881e3a9164abcde0; biz_trace_id=f3f5fc00; bd_ticket_guard_client_data_v2=eyJyZWVfcHVibGljX2tleSI6IkJLS1J3aGlyWXdVcTFTQmdVOUZMRDdPa2I1dGRBemZCREJVWTVpSzY4ck9pdk9SVUk0SC9yYVN6SFFGbDdKcCs4YmpjS2pZc0NHME5SLzc5aVJKSWcvcz0iLCJ0c19zaWduIjoidHMuMi43NDExZDNhNTQ5NzNhMmYzZjUwZDEwMmI1Mjc2YzBhYmEzMDkwMTJhZjA3MTZkYTU4ZmM2NTE4Mzg1YWQ3YzcyYzRmYmU4N2QyMzE5Y2YwNTMxODYyNGNlZGExNDkxMWNhNDA2ZGVkYmViZWRkYjJlMzBmY2U4ZDRmYTAyNTc1ZCIsInJlcV9jb250ZW50Ijoic2VjX3RzIiwicmVxX3NpZ24iOiJVVnJ6dFdabkJYZXc1SjY0MTR3NkxQN2wxYktWZW0xbkRhV2ZuR2xISHRFPSIsInNlY190cyI6IiNQUEVoYTBuY0dtSEZ6M2NsbWpZeDIzUWVzQkp2bWYxc1dMelpIVFgydzNoT1ViZE9pZVlCeXJQdmJuMUEifQ%3D%3D; FOLLOW_LIVE_POINT_INFO=%22MS4wLjABAAAACZf9q5GoAVE5eR4wX1nbLRILQSEwfkHqEOmC-i0wmm6lzx_wnu6-72LJU3-0tN6L%2F1777824000000%2F0%2F1777794748304%2F0%22; __ac_nonce=069f6febe009ca55a40fe; home_can_add_dy_2_desktop=%221%22";
        //String cookie = "enter_pc_once=1; UIFID_TEMP=28ea90c1b0cf804752225259882c701fb12323f08ef828fc1032b615e29efbbe75787543492bebd257f5296f135af5e95a85ef70f4d644b7cd5fcaf50b3c252d30726ed888fc72d08b9b8005a602c20b14958713b76580013a1ea677a38cdafd0bf33bb0119d613755293ac37373a4a1; hevc_supported=true; bd_ticket_guard_client_web_domain=2; fpk1=U2FsdGVkX19LdFCDnsvlepUyu1pUN52B4AfpIu7EU9C7GSadbsYJs+sfSM+82xufF0aGJVCX81J45zieeNQ46Q==; fpk2=0e0369e2813db7deb26e5937c353aab4; UIFID=28ea90c1b0cf804752225259882c701fb12323f08ef828fc1032b615e29efbbe75787543492bebd257f5296f135af5e95a85ef70f4d644b7cd5fcaf50b3c252dca6f93450bcfa0e23994c681fa0cd844a2ac27e1af9c20f72360c1928b32f6a05d09138b25ebee9e7b8392f9955f3a89ecf1cd0ea8a784cdbc7e53508cf1447216873ecdbb1f151b4be58dc7b8c57eb0e1c410201be33fae0cc1ab3adcf48fefb95beef3206de3103ae9ca85eeb300b040b098d1c8d60dc7ad92be6ee00026bf; xgplayer_device_id=99191440500; xgplayer_user_id=588518695396; d_ticket=9d14ab04433a5178efa52a9d662a348e2abb8; is_staff_user=false; live_use_vvc=%22false%22; theme=%22dark%22; manual_theme=%22dark%22; my_rd=2; s_v_web_id=verify_mm6i62jy_PULp6yju_ibeo_4NDm_9l2a_aaWMgaLactr2; passport_csrf_token=d2ec8113c6df622d2295b34e94a9d186; passport_csrf_token_default=d2ec8113c6df622d2295b34e94a9d186; _bd_ticket_crypt_doamin=2; __security_server_data_status=1; passport_mfa_token=CjfNB2nlzV8%2FRNt1fhZ14sOnG%2B7fV4TzOXCQfkPs6SK2wK7gLpgEng4qc0cqay%2F4WAYTNtSQDgRpGkoKPAAAAAAAAAAAAABQI00iDmuGjIK%2FRwjJPBwyMaYSPu%2BLm4%2BFOIlZ6zoXbWXaO84YxVwVqgUCPC5yplr7FBD%2FjYsOGPax0WwgAiIBA6oqFnU%3D; MONITOR_WEB_ID=91997eb8-833e-4dc9-8227-54daef0fe930; dy_swidth=1920; dy_sheight=1080; is_dash_user=1; live_private_user=0; SEARCH_RESULT_LIST_TYPE=%22single%22; use_biz_token=true; has_biz_token=false; download_guide=%223%2F20260403%2F1%22; LivePausePop=%22%257B%2522todayCount%2522%253A1%252C%2522closeNum%2522%253A2%252C%2522todayShowRoom%2522%253A%25227631911633249536795%2522%252C%2522lastTimer%2522%253A1776946971588%257D%22; __live_version__=%221.1.5.1418%22; live_can_add_dy_2_desktop=%221%22; passport_assist_user=CkEOxvmzkFFngujKUQBbe4TIy0tf2UDHjrvTm8qqIg2Ktc5V6ntL__1l7FLwlPS6kwcBcucXkRmCkcUPxbwKVXlonhpKCjwAAAAAAAAAAAAAUFh183Mk9FdiIuUqydEQEtqAfHJsd1mbeC_FoNoZEy1E0ywpHdVro31ISy8lA5bBY0gQ_96PDhiJr9ZUIAEiAQNw5Kcu; n_mh=imZMMIWwPRng1tRBBpq52CaKAFQRTJGogxg7dP1uyAA; sid_guard=0b5fb4bc08d3c6760c67a8404979528c%7C1777103750%7C5184000%7CWed%2C+24-Jun-2026+07%3A55%3A50+GMT; uid_tt=19294c3df7966d1aa2a20aae8f3ee9fa; uid_tt_ss=19294c3df7966d1aa2a20aae8f3ee9fa; sid_tt=0b5fb4bc08d3c6760c67a8404979528c; sessionid=0b5fb4bc08d3c6760c67a8404979528c; sessionid_ss=0b5fb4bc08d3c6760c67a8404979528c; session_tlb_tag=sttt%7C10%7CC1-0vAjTxnYMZ6hASXlSjP_________ULgX2vkuCWMCDk3w7j1Skk5mlAnsFXvJcoeKWYqFbIeE%3D; sid_ucp_v1=1.0.0-KDNhMzExMDBhNmYzYjcyYWQxZWM3Mzk2MTVmYzY1OWQ4ZWNjZWQyNjgKIQjNuLDtpoyHBBCG57HPBhjvMSAMMLC1m5kGOAdA9AdIBBoCbGYiIDBiNWZiNGJjMDhkM2M2NzYwYzY3YTg0MDQ5Nzk1Mjhj; ssid_ucp_v1=1.0.0-KDNhMzExMDBhNmYzYjcyYWQxZWM3Mzk2MTVmYzY1OWQ4ZWNjZWQyNjgKIQjNuLDtpoyHBBCG57HPBhjvMSAMMLC1m5kGOAdA9AdIBBoCbGYiIDBiNWZiNGJjMDhkM2M2NzYwYzY3YTg0MDQ5Nzk1Mjhj; _bd_ticket_crypt_cookie=1b397db7a7ce7dffbaaa324562340aec; login_time=1777103750501; PhoneResumeUidCacheV1=%7B%222283008886053965%22%3A%7B%22time%22%3A1777103752615%2C%22noClick%22%3A1%7D%2C%223182428629500586%22%3A%7B%22time%22%3A1776940852345%2C%22noClick%22%3A1%7D%7D; publish_badge_show_info=%221%2C0%2C0%2C1777107140664%22; stream_recommend_feed_params=%22%7B%5C%22cookie_enabled%5C%22%3Atrue%2C%5C%22screen_width%5C%22%3A1920%2C%5C%22screen_height%5C%22%3A1080%2C%5C%22browser_online%5C%22%3Atrue%2C%5C%22cpu_core_num%5C%22%3A20%2C%5C%22device_memory%5C%22%3A8%2C%5C%22downlink%5C%22%3A10%2C%5C%22effective_type%5C%22%3A%5C%224g%5C%22%2C%5C%22round_trip_time%5C%22%3A0%7D%22; __druidClientInfo=JTdCJTIyY2xpZW50V2lkdGglMjIlM0E1NDklMkMlMjJjbGllbnRIZWlnaHQlMjIlM0E4OTYlMkMlMjJ3aWR0aCUyMiUzQTU0OSUyQyUyMmhlaWdodCUyMiUzQTg5NiUyQyUyMmRldmljZVBpeGVsUmF0aW8lMjIlM0ExJTJDJTIydXNlckFnZW50JTIyJTNBJTIyTW96aWxsYSUyRjUuMCUyMChXaW5kb3dzJTIwTlQlMjAxMC4wJTNCJTIwV2luNjQlM0IlMjB4NjQpJTIwQXBwbGVXZWJLaXQlMkY1MzcuMzYlMjAoS0hUTUwlMkMlMjBsaWtlJTIwR2Vja28pJTIwQ2hyb21lJTJGMTM2LjAuMC4wJTIwU2FmYXJpJTJGNTM3LjM2JTIyJTdE; strategyABtestKey=%221777394292.685%22; ttwid=1%7CmG79TlZI2xO1KhJb3jG3x26-gLcjgyub1Fq-aLx1aSU%7C1777394293%7C351bb4b27e63416c59e0c976d1dd4b6a7dd90e2c63dde1752bd9df57a4d71004; __ac_signature=_02B4Z6wo00f01.4LMtAAAIDCIVDBnEL7tqv-KzZAAJZ8b2; douyin.com; xg_device_score=7.9191272394219645; device_web_cpu_core=20; device_web_memory_size=8; architecture=amd64; is_support_rtm_web_ts=1; home_can_add_dy_2_desktop=%221%22; bd_ticket_guard_client_data=eyJiZC10aWNrZXQtZ3VhcmQtdmVyc2lvbiI6MiwiYmQtdGlja2V0LWd1YXJkLWl0ZXJhdGlvbi12ZXJzaW9uIjoxLCJiZC10aWNrZXQtZ3VhcmQtcmVlLXB1YmxpYy1rZXkiOiJCS0tSd2hpcll3VXExU0JnVTlGTEQ3T2tiNXRkQXpmQkRCVVk1aUs2OHJPaXZPUlVJNEgvcmFTekhRRmw3SnArOGJqY0tqWXNDRzBOUi83OWlSSklnL3M9IiwiYmQtdGlja2V0LWd1YXJkLXdlYi12ZXJzaW9uIjoyfQ%3D%3D; playRecommendGuideTagCount=10; odin_tt=ace0a07e26d8c38baa5919918e9971e197dec424c0cac05e2ea02db09bfff3b312af6a716e3ff48aff7b988015e9810cfa7b18cd71685adb8bc8ff7e227c6ed7; __security_mc_1_s_sdk_crypt_sdk=e97454f7-49a1-9472; __security_mc_1_s_sdk_cert_key=b7a23189-418d-ad2b; __security_mc_1_s_sdk_sign_data_key_web_protect=18b13db6-41ab-aeea; shareRecommendGuideTagCount=4; totalRecommendGuideTagCount=10; gulu_source_res=eyJwX2luIjoiMWNiYzdlMGU3OTFhOTYxMzEyMWJjY2MwOTBiMTgxNzdiNzk5N2Q1MmU1YThhZWZjZDQ0NDI2ZDM5ODZkNmUxZCJ9; sdk_source_info=7e276470716a68645a606960273f276364697660272927676c715a6d6069756077273f276364697660272927666d776a68605a607d71606b766c6a6b5a7666776c7571273f275e58272927666a6b766a69605a696c6061273f27636469766027292762696a6764695a7364776c6467696076273f275e582729277672715a646971273f2763646976602729277f6b5a666475273f2763646976602729276d6a6e5a6b6a716c273f2763646976602729276c6b6f5a7f6367273f27636469766027292771273f27313333363c3630333132323234272927676c715a75776a716a666a69273f2763646976602778; bit_env=DpZAvJ0EEaXccG6Q8zr77WzAF0s-fCuJMR6BI2ChYsUnfsaJPSz75KFV7vMb3Gn7yDjMHOCpxVWtGwkOb6JBmkq7x0b3E7BsPX_NW1uqjIxJVsLR3vGDb4GVr9YTe4GspfNty8iwteph6JbLn49vLQj7A8d5GhSd2i9R7EcdUH5LWIHA2-se8uAcBmZLaASOePIiIOd7y2Ccct8B1VCH-ngpDiQT3ygK-PDPYNUu3im7u-Es1MVAHdPIqeML1e1JF7UGoFAbbfG-Lb-O3nuleTyy3rhbBdNLp69Mu83AT23HiSF8Su4HQiHhjWDf3FyWy7GEJVxEIPjfsnqR0Cfa6hD7L1DmCmf9eVHg_JXcirI9EPzrdowynH93oy7EER0b7L3VXGJWFBFNFRyGYsMxNA78kMk_wewdwry8bpNqTij8ZUX7dfkT-JW20fX_wU81iKxoHnt0YZe1mxW-znL4dY6DMfXaK5WQKra5OqukCR5CofECtronJV1HkUizON3L; passport_auth_mix_state=y4zco1bfvxltzszck97o4ye3hcfyow04; volume_info=%7B%22isMute%22%3Afalse%2C%22isUserMute%22%3Afalse%2C%22volume%22%3A0.35%7D; SelfTabRedDotControl=%5B%7B%22id%22%3A%227612535130670532659%22%2C%22u%22%3A7%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227629523260232697882%22%2C%22u%22%3A8%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227606968062864017434%22%2C%22u%22%3A192%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227513866827342940214%22%2C%22u%22%3A303%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227632196500289767475%22%2C%22u%22%3A124%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227573679794705958950%22%2C%22u%22%3A166%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227624912592858777606%22%2C%22u%22%3A49%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227628932765441853486%22%2C%22u%22%3A5%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227189248543796758585%22%2C%22u%22%3A179%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227613428642962245651%22%2C%22u%22%3A83%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227630435527289276457%22%2C%22u%22%3A39%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227606338889006991411%22%2C%22u%22%3A10%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227573858675698173978%22%2C%22u%22%3A128%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227581885277464741922%22%2C%22u%22%3A38%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227594280000379963442%22%2C%22u%22%3A74%2C%22c%22%3A0%7D%5D; stream_player_status_params=%22%7B%5C%22is_auto_play%5C%22%3A0%2C%5C%22is_full_screen%5C%22%3A0%2C%5C%22is_full_webscreen%5C%22%3A0%2C%5C%22is_mute%5C%22%3A0%2C%5C%22is_speed%5C%22%3A1%2C%5C%22is_visible%5C%22%3A1%7D%22; IsDouyinActive=true; FOLLOW_LIVE_POINT_INFO=%22MS4wLjABAAAAMutji20RI-BreK3_03pd35nDj5AhhEaaLPg7Bh5efyuTIOcDYIVUilE5nBrtifz5%2F1777478400000%2F0%2F0%2F1777466120886%22; FOLLOW_NUMBER_YELLOW_POINT_INFO=%22MS4wLjABAAAAMutji20RI-BreK3_03pd35nDj5AhhEaaLPg7Bh5efyuTIOcDYIVUilE5nBrtifz5%2F1777478400000%2F0%2F1777465520886%2F0%22; biz_trace_id=63c8bb9e; bd_ticket_guard_client_data_v2=eyJyZWVfcHVibGljX2tleSI6IkJLS1J3aGlyWXdVcTFTQmdVOUZMRDdPa2I1dGRBemZCREJVWTVpSzY4ck9pdk9SVUk0SC9yYVN6SFFGbDdKcCs4YmpjS2pZc0NHME5SLzc5aVJKSWcvcz0iLCJ0c19zaWduIjoidHMuMi4zMjY4MWRjNDQ2YmQ0MjI4ZWNhMDBjMTU3MzJhNjM3NDMyZWQ4ZWRhY2NlN2VjN2RlMzcxYmUzNWEyMDZlMzE1YzRmYmU4N2QyMzE5Y2YwNTMxODYyNGNlZGExNDkxMWNhNDA2ZGVkYmViZWRkYjJlMzBmY2U4ZDRmYTAyNTc1ZCIsInJlcV9jb250ZW50Ijoic2VjX3RzIiwicmVxX3NpZ24iOiJNZ3VUUUg0UGtjWHVmd2tIb0NFWVhrR0xyZjBiZG5vQlpLTjdBZUdDWnJVPSIsInNlY190cyI6IiN4NkdPSGNIeS9MeUZXdjlCVzBUbnA3TGNiTU9uT1lIN2lqU0NxVTliY2JiU1dvQVVCM0VkbnRUeVlxTG8ifQ%3D%3D; __ac_nonce=069f1f8c400c3caea90ee";
        headers.put("cookie", cookie);
        headers.put("User-Agent", ClientInfoUtils.getRandomUserAgent());
        HttpRequest get = HttpUtil.createGet("https://www.douyin.com/webcast/web/feed/follow/?device_platform=webapp" +
                "&aid=6383" +
                "&channel=channel_pc_web" +
                "&scene=aweme_pc_follow_top" +
                "&update_version_code=170400" +
                "&pc_client_type=1" +
                "&pc_libra_divert=Windows" +
                "&support_h265=1" +
                "&support_dash=1" +
                "&cpu_core_num=20" +
                "&version_code=170400" +
                "&version_name=17.4.0" +
                "&cookie_enabled=true" +
                "&screen_width=1920" +
                "&screen_height=1080" +
                "&browser_language=zh-CN" +
                "&browser_platform=Win32" +
                "&browser_name=Chrome" +
                "&browser_version=136.0.0.0" +
                "&browser_online=true" +
                "&engine_name=Blink" +
                "&engine_version=136.0.0.0" +
                "&os_name=Windows" +
                "&os_version=10" +
                "&device_memory=8" +
                "&platform=PC" +
                "&downlink=10" +
                "&effective_type=4g" +
                "&round_trip_time=100" +
                "&webid=7507164795549173260&uifid=28ea90c1b0cf804752225259882c701fb12323f08ef828fc1032b615e29efbbeb37ab276c248dcd9d70a89df8e19c1e1319e3109f485cce53e3dbcbcfe49d96de0c0eea6bb0320a4e7d7ecd6fcf7ba23c907a2da4918cc1bc4bd2e1ccf643951e0e8a1afd2bc9db4a8b18ae8771cf9f2bff67ae7f2803ff8d1823b03ff1f9bdba89caf914ab4444c8ba1d30eef9899ff5eeec1c60c5c9d1a361d1346f2ef386b" +
                "&msToken=IRZNgqjWJoWnfBdbrn4BOUcoyjWVLcvrDzDHLCbqHUJAcsbvLrZRXTYKnjfVtijJfayMaONcxoBLzGrkInW8DNk95yhagQG3gLIA15lb8L0dftP_mIcT7GP9c8dt9q783G6InZl_C8x9cXy8gOBpp_y9QU0Uvvj0M64vX-QNRBJ4oQ%3D%3D&a_bogus=dj45g76yxdWROVMtmOD3yRZlOeEMrT8yEPixbwQTHOYBT1FaTmP32ae8coFzK5sVSuZzkI-7Tf4AiVpcOtUiZKFkwmkDSk7j5t%2FCnwvLMHkfT4Jg7ND2CbSEqiTbUSGY8%2FIvE%2F651s0e2E5W9NChApQ7K%2FUnm5jdFr3tV%2Fuji9K4UW8jwn%2Fna3YkLh17&verifyFp=verify_maz2to5l_WJRNPLUq_nzbE_4le3_91vq_QDjkUiNkgZa1&fp=verify_maz2to5l_WJRNPLUq_nzbE_4le3_91vq_QDjkUiNkgZa1");
        HttpResponse execute = get.addHeaders(headers).timeout(3000).execute();
        JSONObject parseObj = JSONUtil.parseObj(execute.body());
        JSONArray jsonArray = parseObj.getJSONObject("data").getJSONArray("data");
        for (Object object : jsonArray) {
            JSONObject jsonObject = (JSONObject) object;
            JSONObject owner = jsonObject.getJSONObject("room").getJSONObject("owner");
            String owner_user_id_str = jsonObject.getJSONObject("room").getStr("owner_user_id_str");
            String title = jsonObject.getJSONObject("room").getStr("title");
            String id_str = owner.getStr("id_str");
            String nickname = owner.getStr("nickname");
            //            log.info("jsonObject:{}", jsonObject);

            if (collect.contains(nickname)) {
                return;
            }

            Long count = userMapper.selectCount(new LambdaQueryWrapper<User>()
                    .eq(User::getUsername, nickname));
            User user = new User();
            user.setAccount(id_str);
            user.setUsername(nickname);
            if (count <= 0) {
                log.info("title:{}, nickname:{}, owner_user_id_str:{}, id_str:{}", title, nickname, owner_user_id_str, id_str);
                user.setPassword(passwordEncoder.encode("123456"));
                user.setSex("女");
                user.setPhone("15725400536");
                user.setEmail(id_str + "@163.com");
                userMapper.insert(user);
                listHandler.rightPush("list:user", JSONUtil.toJsonStr(user));

                Map<String, Object> param = Maps.newHashMap();
                param.put("userId", id_str);
                param.put("nickname", nickname);
                listHandler.rightPush("list:news", JSONUtil.toJsonStr(param));
            }
        }
        XxlJobHelper.log("获取抖音账号关注账号数据完成");
    }

    /**
     * 去除重复数据
     */
    @XxlJob("removeRepeatUser")
    public void removeRepeatUser() {
        List<User> userList = userMapper.getUserList();
        for (User user : userList) {
            ioIntensiveExecutor.execute(() -> {
                List<User> dataList = userMapper.selectList(new LambdaQueryWrapper<User>()
                        .select(User::getId, User::getUsername)
                        .eq(User::getUsername, user.getUsername()));
                if (!CollectionUtils.isEmpty(dataList) && dataList.size() > 1) {
                    List<String> ids = new ArrayList<>(dataList.size() - 1);
                    for (int i = 1; i < dataList.size(); i++) {
                        ids.add(dataList.get(i).getId());
                    }
                    userMapper.deleteBatchIds(ids);
                    XxlJobHelper.log("删除用户:" + user.getUsername());
                    XxlJobHelper.log("删除重复用户数据:" + StringUtils.collectionToDelimitedString(ids, ","));
                }
            });
        }
        XxlJobHelper.log("删除重复用户数据完成");
    }

    /**
     * 获取近7天注册用户列表
     */
    @XxlJob("getRegisteredUserListOf7Day")
    public void getRegisteredUserListOf7Day() {
        Date now = new Date();
        List<User> userList = userMapper.selectList(new LambdaQueryWrapper<User>()
                .select(User::getId, User::getCreateTime)
                .between(User::getCreateTime, DateUtil.offsetDay(now, -7), now)
                .orderByAsc(User::getCreateTime));
        Map<String, Long> userCountMap = userList.stream()
                .filter(user -> user.getCreateTime() != null)
                .collect(Collectors.groupingBy(
                        user -> DateUtil.format(user.getCreateTime(), "yyyy-MM-dd"),
                        // 用 LinkedHashMap 保留排序
                        LinkedHashMap::new,
                        Collectors.counting()
                ))
                // 按 key（日期字符串）自然排序（yyyy-MM-dd 格式支持直接排序）
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldVal, newVal) -> oldVal,
                        LinkedHashMap::new
                ));
        XxlJobHelper.log("获取近7天注册用户列表:{}", userCountMap);
    }

    @PostConstruct
    public void loadData() {
        long start = System.currentTimeMillis();
        stringHandler.delete("list:user");
        List<User> userList = userMapper.getUserList();
        for (User user : userList) {

//            Long size = listHandler.size("list:user");
//            List<Object> objectList = listHandler.range("list:user", 0, size - 1);
//            Set<String> collect = objectList.stream()
//                    .map(obj -> BeanUtil.toBean(obj, User.class))
//                    .map(User::getUsername)
//                    .collect(Collectors.toSet());
//            if (collect.contains(user.getUsername())) {
//                return;
//            }

            if(StrUtil.isNotBlank((user.getAccount()))
                    && StrUtil.isNotBlank((user.getUsername()))
                    && !user.getAccount().equals(user.getUsername())) {
                listHandler.rightPush("list:user", JSONUtil.toJsonStr(user));
            }
        }
        long end = System.currentTimeMillis();
        log.info("加载Redis数据完毕，耗时:{}", (end - start) / 1000);
    }

}
