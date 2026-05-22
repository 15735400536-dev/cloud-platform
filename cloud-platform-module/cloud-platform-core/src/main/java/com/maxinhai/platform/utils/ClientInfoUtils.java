package com.maxinhai.platform.utils;

import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.Random;

/**
 * 客户端信息工具类，用于获取IP地址、MAC地址和解析User-Agent
 */
public class ClientInfoUtils {

    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    /**
     * 全平台：Windows/Mac/Linux/Android/iOS/微信/爬虫 完整UA数组
     */
    private static final String[] USER_AGENT_ARRAY = {
            // Windows Chrome
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36",
            // Windows Edge
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36 Edg/145.0.0.0",
            // Windows Firefox
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:126.0) Gecko/20100101 Firefox/126.0",
            // Windows Opera
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36 OPR/125.0.0.0",
            // IE11
            "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",

            // Mac Chrome
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36",
            // Mac Edge
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36 Edg/145.0.0.0",
            // Mac Safari
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.4 Safari/605.1.15",
            // Mac Firefox
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:126.0) Gecko/20100101 Firefox/126.0",

            // Linux Chrome
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36",
            // Linux Firefox
            "Mozilla/5.0 (X11; Linux x86_64; rv:126.0) Gecko/20100101 Firefox/126.0",

            // Android Chrome
            "Mozilla/5.0 (Linux; Android 14; SM-G991B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.7632.161 Mobile Safari/537.36",
            // Android Edge
            "Mozilla/5.0 (Linux; Android 14; SM-G991B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.7632.161 Mobile Safari/537.36 EdgA/145.0.0.0",
            // 安卓微信浏览器
            "Mozilla/5.0 (Linux; Android 14; SM-G991B) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/122.0.0.0 Mobile Safari/537.36 MicroMessenger/8.0.47.0(0x28002F51) WeChat/arm64",

            // iPhone Safari
            "Mozilla/5.0 (iPhone; CPU iPhone OS 17_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.4 Mobile/15E148 Safari/604.1",
            // iPhone Chrome
            "Mozilla/5.0 (iPhone; CPU iPhone OS 17_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/145.0.7632.124 Mobile/15E148 Safari/604.1",
            // iOS 微信浏览器
            "Mozilla/5.0 (iPhone; CPU iPhone OS 17_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.47(0x28002F51) NetType/WIFI Language/zh_CN",

            // 搜索引擎爬虫
            "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
            "Mozilla/5.0 (Linux; Android 6.0.1; Nexus 5X Build/MMB29P) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Mobile Safari/537.36 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
            "Mozilla/5.0 (compatible; Bingbot/2.0; +http://www.bing.com/bingbot.htm)"
    };

    private static final Random RANDOM = new Random();

    /**
     * 随机获取一个 User-Agent
     * @return 随机UA字符串
     */
    public static String getRandomUserAgent() {
        int index = RANDOM.nextInt(USER_AGENT_ARRAY.length);
        return USER_AGENT_ARRAY[index];
    }

    // 仅信任内网代理（非常重要）
    private static boolean isInnerProxy(String ip) {
        if(Objects.isNull(ip) || ip.isEmpty()) {
            return false;
        }
        return ip.startsWith("10.") || ip.startsWith("192.168.") || ip.startsWith("172.")
                || "127.0.0.1".equals(ip) || "localhost".equals(ip);
    }

    /**
     * 安全获取客户端IP（防伪造，企业级）
     */
    public static String getSafeIpAddress(HttpServletRequest request) {
        // 1. 先拿真实TCP链接IP（不可伪造）
        String realIp = request.getRemoteAddr();

        // 2. 如果是内网代理（Nginx/网关/SLB），才信任X-Forwarded-For
        if (isInnerProxy(realIp)) {
            // 只从可信代理头获取IP
            String xff = request.getHeader("X-Forwarded-For");
            if (StringUtils.hasText(xff) && !"unknown".equalsIgnoreCase(xff)) {
                // 取最后一段，不是第一段！！！（防伪造关键）
                String[] ips = xff.split(",");
                for (int i = ips.length - 1; i >= 0; i--) {
                    String ip = ips[i].trim();
                    if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
                        return ip;
                    }
                }
            }

            // 兼容网关配置 X-Real-IP
            String realIpHeader = request.getHeader("X-Real-IP");
            if (StringUtils.hasText(realIpHeader) && !"unknown".equalsIgnoreCase(realIpHeader)) {
                return realIpHeader;
            }
        }

        // 3. 都没有 → 返回真实TCP IP（最安全）
        return realIp;
    }

    public static String getSafeIpAddress() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return "unknown";
        }
        return getSafeIpAddress(attributes.getRequest());
    }

    /**
     * 获取客户端IP地址
     *
     * @return 客户端IP地址
     */
    public static String getIpAddress() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return "unknown";
        }

        HttpServletRequest request = requestAttributes.getRequest();

        for (String header : IP_HEADER_CANDIDATES) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                // 对于X-Forwarded-For，取第一个IP
                if (ip.contains(",")) {
                    return ip.split(",")[0].trim();
                }
                return ip;
            }
        }

        // 如果没有获取到，则使用request.getRemoteAddr()
        return request.getRemoteAddr();
    }

    /**
     * 获取客户端IP地址
     *
     * @param request 当前请求
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        for (String header : IP_HEADER_CANDIDATES) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                // 对于X-Forwarded-For，取第一个IP
                if (ip.contains(",")) {
                    return ip.split(",")[0].trim();
                }
                return ip;
            }
        }

        // 如果没有获取到，则使用request.getRemoteAddr()
        return request.getRemoteAddr();
    }

    /**
     * 通过IP地址获取MAC地址（仅适用于局域网）
     *
     * @param ipAddress IP地址
     * @return MAC地址，获取失败返回null
     */
    public static String getMacAddress(String ipAddress) {
        if (ipAddress == null || ipAddress.isEmpty()) {
            return null;
        }

        // 本地主机直接获取MAC
        if ("127.0.0.1".equals(ipAddress) || "localhost".equals(ipAddress)) {
            try {
                InetAddress localHost = InetAddress.getLocalHost();
                NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
                byte[] mac = networkInterface.getHardwareAddress();

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }
                return sb.toString();
            } catch (UnknownHostException | SocketException e) {
                e.printStackTrace();
                return null;
            }
        }

        // 对于局域网内的IP，尝试通过arp命令获取MAC（仅适用于Windows系统）
        try {
            Process process = Runtime.getRuntime().exec("arp -a " + ipAddress);
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.startsWith(ipAddress)) {
                        String[] parts = line.split("\\s+");
                        if (parts.length >= 2) {
                            return parts[1];
                        }
                    }
                }
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取客户端MAC地址（简化版）
     *
     * @return MAC地址
     */
    public static String getMacAddress() {
        return getMacAddress(getIpAddress());
    }

    /**
     * 解析User-Agent信息
     *
     * @return 包含浏览器和操作系统信息的字符串
     */
    public static String parseUserAgent() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return "unknown";
        }

        HttpServletRequest request = requestAttributes.getRequest();
        String userAgentString = request.getHeader("User-Agent");

        if (!StringUtils.hasText(userAgentString)) {
            return "unknown";
        }

        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);

        // 获取浏览器信息
        String browser = userAgent.getBrowser().getName() + " " + userAgent.getBrowserVersion();

        // 获取操作系统信息
        String os = userAgent.getOperatingSystem().getName();

        return browser + " / " + os;
    }

    /**
     * 解析User-Agent信息
     *
     * @param request 当前请求
     * @return 包含浏览器和操作系统信息的字符串
     */
    public static String parseUserAgent(HttpServletRequest request) {
        String userAgentString = request.getHeader("User-Agent");

        if (!StringUtils.hasText(userAgentString)) {
            return "unknown";
        }

        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);

        // 获取浏览器信息
        String browser = userAgent.getBrowser().getName() + " " + userAgent.getBrowserVersion();

        // 获取操作系统信息
        String os = userAgent.getOperatingSystem().getName();

        return browser + " / " + os;
    }

    public static ServletRequestAttributes getRequestAttributes() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new IllegalStateException("获取ServletRequestAttributes失败!");
        }
        return requestAttributes;
    }

    /**
     * 获取当前请求
     *
     * @param requestAttributes
     * @return
     */
    public static HttpServletRequest getRequest(ServletRequestAttributes requestAttributes) {
        HttpServletRequest request = requestAttributes.getRequest();
        if (request == null) {
            throw new IllegalStateException("获取HttpServletRequest失败!");
        }
        return request;
    }

}
