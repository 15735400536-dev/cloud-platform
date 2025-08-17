package com.maxinhai.platform.utils;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.*;

import java.util.Map;

public class ServerInfoUtils {

    private static final int OSHI_WAIT_SECOND = 1000;
    private static final DecimalFormat df = new DecimalFormat("#.00");

    public static Map<String, Object> getServerInfo() {
        Map<String, Object> result = new HashMap<>();

        // 获取IP和MAC地址
        Map<String, String> networkInfo = getNetworkInfo();
        result.put("ipAddress", networkInfo.get("ip"));
        result.put("macAddress", networkInfo.get("mac"));

        // 获取系统信息
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hal = systemInfo.getHardware();

        // 获取CPU信息
        result.put("cpu", getCpuInfo(hal.getProcessor()));

        // 获取内存信息
        result.put("memory", getMemoryInfo(hal.getMemory()));

        // 获取JVM信息
        result.put("jvm", getJvmInfo());

        // 获取硬盘信息
        result.put("disk", getDiskInfo(systemInfo.getOperatingSystem()));

        return result;
    }

    /**
     * 获取IP和MAC地址
     */
    private static Map<String, String> getNetworkInfo() {
        Map<String, String> networkInfo = new HashMap<>();
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            networkInfo.put("ip", localHost.getHostAddress());

            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
            byte[] macAddressBytes = networkInterface.getHardwareAddress();
            StringBuilder macAddressBuilder = new StringBuilder();

            for (int i = 0; i < macAddressBytes.length; i++) {
                macAddressBuilder.append(String.format("%02X%s", macAddressBytes[i],
                        (i < macAddressBytes.length - 1) ? "-" : ""));
            }
            networkInfo.put("mac", macAddressBuilder.toString());

        } catch (UnknownHostException | SocketException e) {
            networkInfo.put("ip", "获取失败");
            networkInfo.put("mac", "获取失败");
        }
        return networkInfo;
    }

    /**
     * 获取CPU信息
     */
    private static Map<String, Object> getCpuInfo(CentralProcessor processor) {
        Map<String, Object> cpuInfo = new HashMap<>();

        // CPU型号
        cpuInfo.put("model", processor.getProcessorIdentifier().getName());

        // 核心数
        cpuInfo.put("coreCount", processor.getLogicalProcessorCount());

        // CPU使用率
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(OSHI_WAIT_SECOND);
        long[] ticks = processor.getSystemCpuLoadTicks();

        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long sys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];

        long totalCpu = user + nice + sys + idle + iowait + irq + softirq + steal;
        double usage = 100d * (totalCpu - idle) / totalCpu;
        cpuInfo.put("usage", df.format(usage) + "%");

        return cpuInfo;
    }

    /**
     * 获取内存信息
     */
    private static Map<String, Object> getMemoryInfo(GlobalMemory memory) {
        Map<String, Object> memoryInfo = new HashMap<>();

        // 总内存
        long total = memory.getTotal();
        memoryInfo.put("total", formatSize(total));

        // 已用内存
        long used = total - memory.getAvailable();
        memoryInfo.put("used", formatSize(used));

        // 可用内存
        memoryInfo.put("available", formatSize(memory.getAvailable()));

        // 使用率
        double usage = 100d * used / total;
        memoryInfo.put("usage", df.format(usage) + "%");

        return memoryInfo;
    }

    /**
     * 获取JVM信息
     */
    private static Map<String, Object> getJvmInfo() {
        Map<String, Object> jvmInfo = new HashMap<>();
        Runtime runtime = Runtime.getRuntime();

        // JVM总内存
        long totalMemory = runtime.totalMemory();
        jvmInfo.put("totalMemory", formatSize(totalMemory));

        // JVM已用内存
        long usedMemory = totalMemory - runtime.freeMemory();
        jvmInfo.put("usedMemory", formatSize(usedMemory));

        // JVM最大内存
        long maxMemory = runtime.maxMemory();
        jvmInfo.put("maxMemory", formatSize(maxMemory));

        // JVM内存使用率
        double usage = 100d * usedMemory / maxMemory;
        jvmInfo.put("usage", df.format(usage) + "%");

        // JVM启动时间
        long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        jvmInfo.put("startTime", new Date(startTime));
        jvmInfo.put("uptime", formatUptime(uptime));

        return jvmInfo;
    }

    /**
     * 获取硬盘信息
     */
    private static List<Map<String, Object>> getDiskInfo(OperatingSystem os) {
        List<Map<String, Object>> diskInfos = new ArrayList<>();
        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fileStores = fileSystem.getFileStores();

        for (OSFileStore store : fileStores) {
            try {
                Map<String, Object> diskInfo = new HashMap<>();
                diskInfo.put("name", store.getName());
                diskInfo.put("path", store.getMount());
                diskInfo.put("type", store.getType());

                long totalSpace = store.getTotalSpace();
                diskInfo.put("total", formatSize(totalSpace));

                long usedSpace = totalSpace - store.getUsableSpace();
                diskInfo.put("used", formatSize(usedSpace));

                diskInfo.put("available", formatSize(store.getUsableSpace()));

                double usage = 100d * usedSpace / totalSpace;
                diskInfo.put("usage", df.format(usage) + "%");

                diskInfos.add(diskInfo);
            } catch (Exception e) {
                // 忽略异常的磁盘信息
            }
        }

        return diskInfos;
    }

    /**
     * 格式化大小（B -> KB, MB, GB）
     */
    private static String formatSize(long size) {
        double sizeDouble = size;
        if (sizeDouble < 1024) {
            return df.format(sizeDouble) + " B";
        }
        sizeDouble /= 1024;
        if (sizeDouble < 1024) {
            return df.format(sizeDouble) + " KB";
        }
        sizeDouble /= 1024;
        if (sizeDouble < 1024) {
            return df.format(sizeDouble) + " MB";
        }
        sizeDouble /= 1024;
        return df.format(sizeDouble) + " GB";
    }

    /**
     * 格式化运行时间
     */
    private static String formatUptime(long uptime) {
        long days = uptime / (24 * 60 * 60 * 1000);
        uptime %= (24 * 60 * 60 * 1000);
        long hours = uptime / (60 * 60 * 1000);
        uptime %= (60 * 60 * 1000);
        long minutes = uptime / (60 * 1000);
        uptime %= (60 * 1000);
        long seconds = uptime / 1000;

        return String.format("%d天%d时%d分%d秒", days, hours, minutes, seconds);
    }

}
