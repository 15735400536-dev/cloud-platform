package com.maxinhai.platform.controller;

import cn.hutool.core.util.NumberUtil;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.vo.report.ServerMonitorVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/server")
@Api(tags = "服务器接口")
public class ServerController {

    private final SystemInfo systemInfo = new SystemInfo();
    private static final ServerMonitorVO serverMonitor = new ServerMonitorVO();

    /**
     * 获取服务器监控数据（对接大屏前端）
     */
    @GetMapping("/monitor")
    @ApiOperation(value = "获取服务器监控数据", notes = "获取服务器监控数据")
    public AjaxResult<ServerMonitorVO> getServerMonitor() {
        return AjaxResult.success(serverMonitor);
    }

    // 字节转 GB
    private double byteToGb(long bytes) {
        return bytes * 1.0 / 1024 / 1024 / 1024;
    }

    @Scheduled(fixedRate = 30000)
    public void updateSchedule() throws InterruptedException {
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        OperatingSystem os = systemInfo.getOperatingSystem();

        // ========== 1. Java 版本 ==========
        String javaVersion = System.getProperty("java.version");
        serverMonitor.setJavaVersion(javaVersion);

        // ========== 2. CPU 核心数 ==========
        CentralProcessor processor = hardware.getProcessor();
        int cpuCore = processor.getLogicalProcessorCount();
        serverMonitor.setCpuCore(cpuCore);

        // ========== 3. CPU 使用率 ==========
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        double cpuLoad = processor.getSystemCpuLoadBetweenTicks(prevTicks);
        int cpuUsage = (int) (cpuLoad * 100);
        serverMonitor.setCpuUsage(cpuUsage);

        // ========== 4. 内存信息 ==========
        GlobalMemory memory = hardware.getMemory();
        long memTotalBytes = memory.getTotal();
        long memAvailableBytes = memory.getAvailable();
        long memUsedBytes = memTotalBytes - memAvailableBytes;

        double memTotalGb = byteToGb(memTotalBytes);
        double memUsedGb = byteToGb(memUsedBytes);
        int memUsage = NumberUtil.round(memUsedGb / memTotalGb * 100, 0).intValue();

        serverMonitor.setMemTotal(NumberUtil.round(memTotalGb, 1).doubleValue());
        serverMonitor.setMemUsed(NumberUtil.round(memUsedGb, 1).doubleValue());
        serverMonitor.setMemUsage(memUsage);

        // ========== 5. 硬盘信息 ==========
        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fileStores = fileSystem.getFileStores();
        long diskTotal = 0;
        long diskUsed = 0;
        for (OSFileStore fs : fileStores) {
            diskTotal += fs.getTotalSpace();
            diskUsed += (fs.getTotalSpace() - fs.getUsableSpace());
        }
        double diskTotalGb = byteToGb(diskTotal);
        double diskUsedGb = byteToGb(diskUsed);
        int diskUsage = NumberUtil.round(diskUsedGb / diskTotalGb * 100, 0).intValue();

        serverMonitor.setDiskTotal(NumberUtil.round(diskTotalGb, 1).doubleValue());
        serverMonitor.setDiskUsed(NumberUtil.round(diskUsedGb, 1).doubleValue());
        serverMonitor.setDiskUsage(diskUsage);

        // ========== 6. JVM 内存占用 ==========
        long jvmMax = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax();
        long jvmUsed = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
        int jvmUsage = NumberUtil.round((double) jvmUsed / jvmMax * 100, 0).intValue();
        serverMonitor.setJvmUsage(jvmUsage);

        // ========== 7. 网络带宽（模拟，如需真实可扩展） ==========
        serverMonitor.setNetUsage(ThreadLocalRandom.current().nextInt(10, 40));
//        int netUsage = calculateNetUsage();
//        serverMonitor.setNetUsage(netUsage);
    }

    /**
     * 计算真实网络带宽使用率（oshi 6.4.0 专用）
     */
    private int calculateNetUsage() throws InterruptedException {
        // 获取所有网卡
        List<NetworkIF> networks = systemInfo.getHardware().getNetworkIFs();

        long firstRecv = 0;
        long firstSend = 0;
        long speed = 0;

        // 第一次采集数据
        for (NetworkIF net : networks) {
            net.updateAttributes();
            // 只取正常运行、非虚拟、有流量的网卡
            if (net.getIfOperStatus() == NetworkIF.IfOperStatus.UP
                    && !net.isKnownVmMacAddr()
                    && net.getBytesRecv() > 0) {

                firstRecv += net.getBytesRecv();
                firstSend += net.getBytesSent();
                speed = net.getSpeed();
            }
        }

        // 等待1秒
        Thread.sleep(1000);

        // 第二次采集
        long secondRecv = 0;
        long secondSend = 0;
        for (NetworkIF net : networks) {
            net.updateAttributes();
            if (net.getIfOperStatus() == NetworkIF.IfOperStatus.UP
                    && !net.isKnownVmMacAddr()
                    && net.getBytesRecv() > 0) {

                secondRecv += net.getBytesRecv();
                secondSend += net.getBytesSent();
            }
        }

        // 1秒内收发流量
        long recvDelta = secondRecv - firstRecv;
        long sendDelta = secondSend - firstSend;
        long totalBytesPerSecond = recvDelta + sendDelta;

        // 转成 Mbps （字节 * 8 = 比特）
        double currentMbps = totalBytesPerSecond * 8.0 / 1_000_000;
        double maxMbps = speed * 1.0 / 1_000_000;

        if (maxMbps <= 0) {
            return ThreadLocalRandom.current().nextInt(5, 25); // 虚拟环境 fallback
        }

        // 使用率
        int usage = (int) (currentMbps / maxMbps * 100);
        return Math.min(usage, 100);
    }

}
