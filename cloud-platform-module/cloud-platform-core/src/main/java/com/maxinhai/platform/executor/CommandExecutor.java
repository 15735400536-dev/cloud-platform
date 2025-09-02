package com.maxinhai.platform.executor;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName：CommandExecutor
 * @Author: XinHai.Ma
 * @Date: 2025/9/2 11:26
 * @Description: cmd/shell命令执行器
 */
@Component
public class CommandExecutor {

    /**
     * 执行命令并返回结果
     * @param command 命令字符串
     * @param isWindows 是否为Windows系统
     * @return 命令执行结果
     * @throws IOException 执行异常
     * @throws InterruptedException 中断异常
     */
    public CommandResult execute(String command, boolean isWindows) throws IOException, InterruptedException {
        List<String> commands = new ArrayList<>();

        // 根据操作系统选择执行器
        if (isWindows) {
            commands.add("cmd");
            commands.add("/c");
        } else {
            commands.add("/bin/sh");
            commands.add("-c");
        }
        commands.add(command);

        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        // 合并错误流到输出流，方便统一处理
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        // 读取输出
        List<String> output = readStream(process.getInputStream());

        // 等待命令执行完成
        int exitCode = process.waitFor();

        return new CommandResult(exitCode, output);
    }

    /**
     * 读取输入流内容
     */
    private List<String> readStream(InputStream inputStream) throws IOException {
        List<String> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
        }
        return result;
    }

    /**
     * 命令执行结果封装类
     */
    public static class CommandResult {
        private final int exitCode;
        private final List<String> output;

        public CommandResult(int exitCode, List<String> output) {
            this.exitCode = exitCode;
            this.output = output;
        }

        // getter方法
        public int getExitCode() {
            return exitCode;
        }

        public List<String> getOutput() {
            return output;
        }

        @Override
        public String toString() {
            return "Exit Code: " + exitCode + "\nOutput: " + String.join("\n", output);
        }
    }

}
