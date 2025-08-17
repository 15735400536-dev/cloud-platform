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
     * 获取客户端IP地址
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
     * 通过IP地址获取MAC地址（仅适用于局域网）
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
     * @return MAC地址
     */
    public static String getMacAddress() {
        return getMacAddress(getIpAddress());
    }

    /**
     * 解析User-Agent信息
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

}
