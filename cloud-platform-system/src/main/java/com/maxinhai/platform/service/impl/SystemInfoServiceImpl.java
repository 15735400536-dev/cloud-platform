package com.maxinhai.platform.service.impl;

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
        String cookie = "enter_pc_once=1; UIFID_TEMP=28ea90c1b0cf804752225259882c701fb12323f08ef828fc1032b615e29efbbea06dc38c89faf9df392d4bd20a6e390645a731204708193bbdd23f4349c026fb59720c5db1bb4a1ae643f5b0553e6c46; hevc_supported=true; dy_swidth=1920; dy_sheight=1080; fpk1=U2FsdGVkX19vjvEtvh4IlyCaxOUeaVblEpRvSlAW2U4lI2K1I0FfOQSp4byxdKvj8SJaRukmvTzywA8qtRJ3+g==; fpk2=0e0369e2813db7deb26e5937c353aab4; s_v_web_id=verify_me448dy7_Tfsde2Dm_6GEq_4kA7_9NzQ_6CmLstNqPFgr; xgplayer_device_id=20656154195; xgplayer_user_id=604362628617; UIFID=28ea90c1b0cf804752225259882c701fb12323f08ef828fc1032b615e29efbbea06dc38c89faf9df392d4bd20a6e390698aaaf09c1da46c04218de191c80d221fe532392b4606414a0b2a493d09caec3c4d1b6344fa95803422be3dacef30f6349974c9812a0ce31f1ac29d99073933c892d41e32c5db9100f1bceb96b565e86bd2318171bd7379a6c67555beb193bc8759d00e02597b116af2722fdbb933fcf; is_dash_user=1; __security_mc_1_s_sdk_crypt_sdk=9bcebcc0-4356-883b; bd_ticket_guard_client_web_domain=2; passport_csrf_token=87ab3b391ef393f7537f0253dd6416c3; passport_csrf_token_default=87ab3b391ef393f7537f0253dd6416c3; d_ticket=afc14ce956f0df67d5084fa8568757c8ee8d0; n_mh=imZMMIWwPRng1tRBBpq52CaKAFQRTJGogxg7dP1uyAA; uid_tt=fdb85ce2161b28f4cbe7ea93e669e031; uid_tt_ss=fdb85ce2161b28f4cbe7ea93e669e031; sid_tt=80e60b1699453db025097dcf54008303; sessionid=80e60b1699453db025097dcf54008303; sessionid_ss=80e60b1699453db025097dcf54008303; is_staff_user=false; __security_mc_1_s_sdk_cert_key=7bb277c8-432a-b3d1; __security_server_data_status=1; live_use_vvc=%22false%22; h265ErrorNum=-1; passport_mfa_token=CjcD4a%2BkCCbXTPTw9%2B621UJUz8QLYvmk3HVwql6SryKbmLlWD%2BMbnmR7rlxlE0%2B2bF5vghLtcZVRGkoKPAAAAAAAAAAAAABPVnjfMC0PRuiQFcIoKotjXdRycWHrj4r5Qj0E2uibC3nUVLV9jHP%2FlPLC2Iduj5R8CxDsjfkNGPax0WwgAiIBA4x%2BXP8%3D; SEARCH_RESULT_LIST_TYPE=%22single%22; __druidClientInfo=JTdCJTIyY2xpZW50V2lkdGglMjIlM0EzMDQlMkMlMjJjbGllbnRIZWlnaHQlMjIlM0E2ODUlMkMlMjJ3aWR0aCUyMiUzQTMwNCUyQyUyMmhlaWdodCUyMiUzQTY4NSUyQyUyMmRldmljZVBpeGVsUmF0aW8lMjIlM0ExJTJDJTIydXNlckFnZW50JTIyJTNBJTIyTW96aWxsYSUyRjUuMCUyMChXaW5kb3dzJTIwTlQlMjAxMC4wJTNCJTIwV2luNjQlM0IlMjB4NjQpJTIwQXBwbGVXZWJLaXQlMkY1MzcuMzYlMjAoS0hUTUwlMkMlMjBsaWtlJTIwR2Vja28pJTIwQ2hyb21lJTJGMTM2LjAuMC4wJTIwU2FmYXJpJTJGNTM3LjM2JTIyJTdE; theme=%22light%22; shareRecommendGuideTagCount=3; my_rd=2; download_guide=%220%2F%2F1%22; publish_badge_show_info=%221%2C0%2C0%2C1757500613284%22; passport_assist_user=CkGeDtPI1nBUNesvHD8mRDFqGXcWBnTvYI1Iq5t-BpvLyUEcpT6Ubb20C93_izX0m_Ed07NwHk5DYQVCczZP3XPTIBpKCjwAAAAAAAAAAAAAT3VlXIqY9TWAGl3W57keNN3mEJz0f1RFpe4XrPA-Ife_ZkYAcYyg51BxKsneEApxQRQQ9-f7DRiJr9ZUIAEiAQP6NSYd; sid_guard=80e60b1699453db025097dcf54008303%7C1757511794%7C5184000%7CSun%2C+09-Nov-2025+13%3A43%3A14+GMT; sid_ucp_v1=1.0.0-KDA2ODM3MDUxOTE1OTdjY2JlYzIwODFlN2MxMjFlMjI2MTYzZGIyODQKIQjNuLDtpoyHBBDygIbGBhjvMSAMMLC1m5kGOAVA-wdIBBoCbGYiIDgwZTYwYjE2OTk0NTNkYjAyNTA5N2RjZjU0MDA4MzAz; ssid_ucp_v1=1.0.0-KDA2ODM3MDUxOTE1OTdjY2JlYzIwODFlN2MxMjFlMjI2MTYzZGIyODQKIQjNuLDtpoyHBBDygIbGBhjvMSAMMLC1m5kGOAVA-wdIBBoCbGYiIDgwZTYwYjE2OTk0NTNkYjAyNTA5N2RjZjU0MDA4MzAz; login_time=1757511793202; __security_mc_1_s_sdk_sign_data_key_web_protect=165812a2-426d-b78a; _bd_ticket_crypt_cookie=7b147e12024e5bee736bd7bd34f98cf2; __ac_nonce=068c2addc0007cdb041a6; __ac_signature=_02B4Z6wo00f01ccwlzAAAIDAGGtkfbodfo3HEJOAABkQae; douyin.com; xg_device_score=7.818598560905933; device_web_cpu_core=20; device_web_memory_size=8; architecture=amd64; home_can_add_dy_2_desktop=%220%22; stream_recommend_feed_params=%22%7B%5C%22cookie_enabled%5C%22%3Atrue%2C%5C%22screen_width%5C%22%3A1920%2C%5C%22screen_height%5C%22%3A1080%2C%5C%22browser_online%5C%22%3Atrue%2C%5C%22cpu_core_num%5C%22%3A20%2C%5C%22device_memory%5C%22%3A8%2C%5C%22downlink%5C%22%3A10%2C%5C%22effective_type%5C%22%3A%5C%224g%5C%22%2C%5C%22round_trip_time%5C%22%3A50%7D%22; SelfTabRedDotControl=%5B%7B%22id%22%3A%227321354226083301387%22%2C%22u%22%3A313%2C%22c%22%3A309%7D%2C%7B%22id%22%3A%227527666382473791527%22%2C%22u%22%3A14%2C%22c%22%3A14%7D%5D; strategyABtestKey=%221757588961.033%22; __live_version__=%221.1.3.9904%22; webcast_local_quality=null; live_can_add_dy_2_desktop=%221%22; bd_ticket_guard_client_data=eyJiZC10aWNrZXQtZ3VhcmQtdmVyc2lvbiI6MiwiYmQtdGlja2V0LWd1YXJkLWl0ZXJhdGlvbi12ZXJzaW9uIjoxLCJiZC10aWNrZXQtZ3VhcmQtcmVlLXB1YmxpYy1rZXkiOiJCS0tSd2hpcll3VXExU0JnVTlGTEQ3T2tiNXRkQXpmQkRCVVk1aUs2OHJPaXZPUlVJNEgvcmFTekhRRmw3SnArOGJqY0tqWXNDRzBOUi83OWlSSklnL3M9IiwiYmQtdGlja2V0LWd1YXJkLXdlYi12ZXJzaW9uIjoyfQ%3D%3D; volume_info=%7B%22isUserMute%22%3Afalse%2C%22isMute%22%3Afalse%2C%22volume%22%3A0.233%7D; gulu_source_res=eyJwX2luIjoiMWNiYzdlMGU3OTFhOTYxMzEyMWJjY2MwOTBiMTgxNzdiNzk5N2Q1MmU1YThhZWZjZDQ0NDI2ZDM5ODZkNmUxZCJ9; WallpaperGuide=%7B%22showTime%22%3A0%2C%22closeTime%22%3A0%2C%22showCount%22%3A0%2C%22cursor1%22%3A202%2C%22cursor2%22%3A72%2C%22hoverTime%22%3A1754879694406%7D; odin_tt=acb8f414c022a61a65e99b0266a729b0008687bc493d1abe0be2be9cafb663c3e8037af2b43e675cfc18c27d365a37b1d539c771a73a9aed8ee1869275e449199c6ae48359eb4b702a50a06dca252afe; FOLLOW_LIVE_POINT_INFO=%22MS4wLjABAAAAMutji20RI-BreK3_03pd35nDj5AhhEaaLPg7Bh5efyuTIOcDYIVUilE5nBrtifz5%2F1757606400000%2F0%2F0%2F1757590927434%22; FOLLOW_NUMBER_YELLOW_POINT_INFO=%22MS4wLjABAAAAMutji20RI-BreK3_03pd35nDj5AhhEaaLPg7Bh5efyuTIOcDYIVUilE5nBrtifz5%2F1757606400000%2F0%2F0%2F1757591527435%22; stream_player_status_params=%22%7B%5C%22is_auto_play%5C%22%3A0%2C%5C%22is_full_screen%5C%22%3A0%2C%5C%22is_full_webscreen%5C%22%3A0%2C%5C%22is_mute%5C%22%3A0%2C%5C%22is_speed%5C%22%3A1%2C%5C%22is_visible%5C%22%3A1%7D%22; IsDouyinActive=true; playRecommendGuideTagCount=1; totalRecommendGuideTagCount=18; biz_trace_id=4878cacf; ttwid=1%7CH8ClIisyQoLCSznqG7alYZdyu7I2uBLTYR8_l3FuQtA%7C1757590572%7C463c37a758ad69318ebe4db19e21a4aaac9b416c7504b42de7430c787e97f15c; sdk_source_info=7e276470716a68645a606960273f276364697660272927676c715a6d6069756077273f276364697660272927666d776a68605a607d71606b766c6a6b5a7666776c7571273f275e58272927666a6b766a69605a696c6061273f27636469766027292762696a6764695a7364776c6467696076273f275e582729277672715a646971273f2763646976602729277f6b5a666475273f2763646976602729276d6a6e5a6b6a716c273f2763646976602729276c6b6f5a7f6367273f27636469766027292771273f2733323d363230353c3032303234272927676c715a75776a716a666a69273f2763646976602778; bit_env=ZGbHmhZEgWNzk5_3AF_bVJn0RXyVuwhfmKEJYiv7hxv_HQHm1Td1EfIoW70nRuzxPCLELc8__-F84e49jlYuwCTQjnfsmoK-JGmSqFJX5o7f4dtG2AaIhfw_n6H0tShDZG1yPujCrKJBSiBeBsKZyp5Fj-8wPbP708UxSlwSMzYHLCz870ZXS7CdeIglTPFuGr8Zv87KEwyP9XGtAIBP39K6gXBcsui7KwfXQGOkpHtsj9atKqIHCgc6FoVMsNVyU2xtGCCMbblxvyt36TVE0z2eTc2hgLtYBftPzHKS7MlfrKPsUSw_qPG38zc07vaa5F8yZBr5MRhcC_-SgPDuSykvNGSFvHVxnqXW-wozzntinBjg71kxdUNlv7VRjVy15YTVw-gmzYC2X9C_vmR3xXUKAJXl8OCFoCnovR9pC3vTAkw_rqOtiYD63guxe3VxxokpByjPpXC29xHoxZ8bYYEPU-1rZM_OsWF2Fjdcc0GQ79j70OdvpSJx_QbUOmNi; passport_auth_mix_state=i3qaduh35xua3k7caln3tcwweyuvfrm2; session_tlb_tag=sttt%7C4%7CgOYLFplFPbAlCX3PVACDA__________J-U400AxJxpxr7aXwUh9gfMXXXLMX7Yn5dYfmdzars2M%3D";
        headers.put("cookie", cookie
//                "UIFID_TEMP=28ea90c1b0cf804752225259882c701fb12323f08ef828fc1032b615e29efbbeb37ab276c248dcd9d70a89df8e19c1e19abd6f1370acc9cce48b2a1789e7271e3be1d10055f781b688493a45345ba1d6; " +
//                "hevc_supported=true; " +
//                "fpk1=U2FsdGVkX1+yeBgdTBZMKE+EGaWJt1gD6jFAsjpL8RpEVlKch4MrSeRjW3BjqZwfcf3mGkk/dxxqVYRDeBBR1Q==; " +
//                "fpk2=0e0369e2813db7deb26e5937c353aab4; " +
//                "s_v_web_id=verify_maz2to5l_WJRNPLUq_nzbE_4le3_91vq_QDjkUiNkgZa1; " +
//                "xgplayer_user_id=953299057652; " +
//                "passport_csrf_token=6526a505d81f0f107d6aa527dfd235ef; " +
//                "passport_csrf_token_default=6526a505d81f0f107d6aa527dfd235ef; " +
//                "__security_mc_1_s_sdk_crypt_sdk=a8b13d34-4011-a38a; " +
//                "bd_ticket_guard_client_web_domain=2; " +
//                "d_ticket=152547615c2034bcc859ca58aeda64bbd0c38; " +
//                "n_mh=imZMMIWwPRng1tRBBpq52CaKAFQRTJGogxg7dP1uyAA; " +
//                "uid_tt=15f18155fcbd2a38137589d232160509; " +
//                "uid_tt_ss=15f18155fcbd2a38137589d232160509; " +
//                "sid_tt=02077a7248958cb7a3e5da9da146cf5a; " +
//                "sessionid=02077a7248958cb7a3e5da9da146cf5a; " +
//                "sessionid_ss=02077a7248958cb7a3e5da9da146cf5a; " +
//                "is_staff_user=false; " +
//                "UIFID=28ea90c1b0cf804752225259882c701fb12323f08ef828fc1032b615e29efbbeb37ab276c248dcd9d70a89df8e19c1e1319e3109f485cce53e3dbcbcfe49d96de0c0eea6bb0320a4e7d7ecd6fcf7ba23c907a2da4918cc1bc4bd2e1ccf643951e0e8a1afd2bc9db4a8b18ae8771cf9f2bff67ae7f2803ff8d1823b03ff1f9bdba89caf914ab4444c8ba1d30eef9899ff5eeec1c60c5c9d1a361d1346f2ef386b; __security_mc_1_s_sdk_cert_key=04435e17-47f9-8cbc; " +
//                "__security_server_data_status=1; " +
//                "live_use_vvc=%22false%22; " +
//                "xgplayer_device_id=41033359029; " +
//                "SEARCH_RESULT_LIST_TYPE=%22single%22; " +
//                "enter_pc_once=1; dy_swidth=1920; " +
//                "dy_sheight=1080; " +
//                "SelfTabRedDotControl=%5B%7B%22id%22%3A%227321354226083301387%22%2C%22u%22%3A271%2C%22c%22%3A0%7D%2C%7B%22id%22%3A%227202211451992279098%22%2C%22u%22%3A869%2C%22c%22%3A0%7D%5D; " +
//                "is_dash_user=1; " +
//                "download_guide=%220%2F%2F1%22; " +
//                "publish_badge_show_info=%221%2C0%2C0%2C1750499695432%22; my_rd=2; passport_mfa_token=CjfFvU%2FIC5BTGja74qCwy7vgOJXSpqtw7GGW8zJO2yoa33jQ6Oyq0xgf7Xt7GtQUR6nhvBvTdU%2F5GkoKPAAAAAAAAAAAAABPJBseIPZReDzd6Rq8sRP1hJ6oJtSB3ZiIIsLq710W3TpAeBtR4K6Zh6eGeTyZsLe1HxC62PQNGPax0WwgAiIBA8XLyyY%3D; passport_assist_user=CkHwOPwfKF9RwdWJlcyPYMtz8nQjO_z4mh-HYG0DY0yWdryD2GoZIyditpHZtY3uy-aQCV6J6nh7CjhMWOy5EGA4LBpKCjwAAAAAAAAAAAAATyQoZHRjiUZmJUMTgoVdh3S7pnEwcMPpIOCl8eMITIWmOm37zHFJkYTxagfwmVRvWfAQ49n0DRiJr9ZUIAEiAQM3NQ3g; sid_guard=02077a7248958cb7a3e5da9da146cf5a%7C1750502080%7C5184000%7CWed%2C+20-Aug-2025+10%3A34%3A40+GMT; sid_ucp_v1=1.0.0-KGMzMTFlNTNkZGU3YzFlMjNjMDhhZThjYjhmMDdmOWZkYjJmMjZmNDAKIQjNuLDtpoyHBBDAldrCBhjvMSAMMLC1m5kGOAVA-wdIBBoCaGwiIDAyMDc3YTcyNDg5NThjYjdhM2U1ZGE5ZGExNDZjZjVh; ssid_ucp_v1=1.0.0-KGMzMTFlNTNkZGU3YzFlMjNjMDhhZThjYjhmMDdmOWZkYjJmMjZmNDAKIQjNuLDtpoyHBBDAldrCBhjvMSAMMLC1m5kGOAVA-wdIBBoCaGwiIDAyMDc3YTcyNDg5NThjYjdhM2U1ZGE5ZGExNDZjZjVh; login_time=1750502080060; _bd_ticket_crypt_cookie=83086c40c27b98995a369186cb112d99; __security_mc_1_s_sdk_sign_data_key_web_protect=96887c93-4ee9-acc4; __live_version__=%221.1.3.4367%22; live_can_add_dy_2_desktop=%221%22; douyin.com; device_web_cpu_core=20; device_web_memory_size=8; architecture=amd64; volume_info=%7B%22isUserMute%22%3Atrue%2C%22isMute%22%3Atrue%2C%22volume%22%3A0.27%7D; xg_device_score=7.959232185235807; WallpaperGuide=%7B%22showTime%22%3A0%2C%22closeTime%22%3A0%2C%22showCount%22%3A0%2C%22cursor1%22%3A22%2C%22cursor2%22%3A6%7D; stream_player_status_params=%22%7B%5C%22is_auto_play%5C%22%3A0%2C%5C%22is_full_screen%5C%22%3A0%2C%5C%22is_full_webscreen%5C%22%3A0%2C%5C%22is_mute%5C%22%3A1%2C%5C%22is_speed%5C%22%3A1%2C%5C%22is_visible%5C%22%3A0%7D%22; passport_fe_beating_status=true; strategyABtestKey=%221750609146.469%22; biz_trace_id=9500ca6b; ttwid=1%7CdPPJTBfQaRJEZASO4cVi76DOpBTDAg4oOx4H0yWgdyM%7C1750609147%7C5ad5df423e3c121ba5818c65a8508ab088df0207a4b08e94d3a6f71cf32d7f66; FOLLOW_LIVE_POINT_INFO=%22MS4wLjABAAAAMutji20RI-BreK3_03pd35nDj5AhhEaaLPg7Bh5efyuTIOcDYIVUilE5nBrtifz5%2F1750694400000%2F0%2F0%2F1750613328038%22; FOLLOW_NUMBER_YELLOW_POINT_INFO=%22MS4wLjABAAAAMutji20RI-BreK3_03pd35nDj5AhhEaaLPg7Bh5efyuTIOcDYIVUilE5nBrtifz5%2F1750694400000%2F0%2F1750612728038%2F0%22; __ac_nonce=068583b2f0089cf44acae; __ac_signature=_02B4Z6wo00f01VsdvXQAAIDAhEZOOuHETB1bPbnAAD6j8e; stream_recommend_feed_params=%22%7B%5C%22cookie_enabled%5C%22%3Atrue%2C%5C%22screen_width%5C%22%3A1920%2C%5C%22screen_height%5C%22%3A1080%2C%5C%22browser_online%5C%22%3Atrue%2C%5C%22cpu_core_num%5C%22%3A20%2C%5C%22device_memory%5C%22%3A8%2C%5C%22downlink%5C%22%3A10%2C%5C%22effective_type%5C%22%3A%5C%224g%5C%22%2C%5C%22round_trip_time%5C%22%3A50%7D%22; bd_ticket_guard_client_data=eyJiZC10aWNrZXQtZ3VhcmQtdmVyc2lvbiI6MiwiYmQtdGlja2V0LWd1YXJkLWl0ZXJhdGlvbi12ZXJzaW9uIjoxLCJiZC10aWNrZXQtZ3VhcmQtcmVlLXB1YmxpYy1rZXkiOiJCQ0pvWGxBa2VUSGRPNDZ6S3FldjFPSXUwVno5dithS01HUSthcGtLN3U2NU42bW5ET0p1MW5aeXU1eURTT2NkOTF4SzdNcmp1TWgzUXZiZjk3ZVB1Uk09IiwiYmQtdGlja2V0LWd1YXJkLXdlYi12ZXJzaW9uIjoyfQ%3D%3D; home_can_add_dy_2_desktop=%221%22; IsDouyinActive=true; odin_tt=8eced4d0c978142ece43b878866999f9e3983eece99d41aed4b2b41f0d2d0a83970a1c0ec5ef946c5107fefe696c4d9d69955f13c063c7b42f4098032bc22dc4"
        );
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
            log.info("title:{}, nickname:{}, owner_user_id_str:{}, id_str:{}", title, nickname, owner_user_id_str, id_str);
//            log.info("jsonObject:{}", jsonObject);

            if (collect.contains(nickname)) {
                return;
            }

            Long count = userMapper.selectCount(new LambdaQueryWrapper<User>()
//                    .select(User::getId, User::getUsername)
                    .eq(User::getUsername, nickname));
            User user = new User();
            user.setAccount(id_str);
            user.setUsername(nickname);
            if (count <= 0) {
                user.setPassword(passwordEncoder.encode("123456"));
                user.setSex("未知");
                user.setPhone("15725400536");
                user.setEmail("15735400536@163.com");
                userMapper.insert(user);
                listHandler.rightPush("list:user", JSONUtil.toJsonStr(user));

                Map param = Maps.newHashMap();
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

            listHandler.rightPush("list:user", JSONUtil.toJsonStr(user));
        }
        long end = System.currentTimeMillis();
        log.info("加载Redis数据完毕，耗时:{}", (end - start) / 1000);
    }

}
