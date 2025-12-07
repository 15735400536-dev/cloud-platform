package com.maxinhai.platform.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;

/**
 * 基于Hutool 5.8.39的图片与Base64互转工具类
 * 适配版本：Hutool 5.8.30+（解决encodeFile和stripPrefix方法爆红问题）
 * 特性：1. 独立无框架依赖 2. 兼容Windows/Mac/Linux 3. 支持自定义保存路径 4. 完善参数校验
 */
public class ImageBase64Utils {

    // ------------------------------ 常量：Base64数据头前缀 ------------------------------
    private static final String BASE64_IMAGE_PREFIX = "data:image/";
    private static final String BASE64_SUFFIX = ";base64,";

    // ------------------------------------------------------------------------
    // 功能1：本地图片文件 → Base64编码（支持带数据头/不带数据头）
    // ------------------------------------------------------------------------

    /**
     * 本地图片转Base64（带数据头，如 data:image/png;base64,xxx）
     *
     * @param imageFilePath 本地图片绝对路径（例：D:/test/1.png 或 /Users/test/2.jpg 或 /home/test/3.png）
     * @return 带格式数据头的Base64字符串
     * @throws IllegalArgumentException 参数非法时抛出
     * @throws IOException              图片读取失败时抛出
     */
    public static String imageToBase64(String imageFilePath) throws IOException {
        // 1. 参数校验
        if (StrUtil.isBlank(imageFilePath)) {
            throw new IllegalArgumentException("图片路径不能为空！");
        }

        // 2. 校验文件是否存在且为有效文件（Hutool自动兼容多系统路径）
        File imageFile = FileUtil.file(imageFilePath);
        if (!imageFile.exists()) {
            throw new IOException("图片文件不存在：" + imageFilePath);
        }
        if (!imageFile.isFile()) {
            throw new IOException("路径不是有效文件（可能是文件夹）：" + imageFilePath);
        }

        // 3. 获取图片后缀（用于拼接数据头，如 png、jpg）
        String fileExt = FileUtil.extName(imageFile).toLowerCase();
        if (StrUtil.isBlank(fileExt)) {
            throw new IOException("图片文件无后缀，无法识别格式：" + imageFilePath);
        }

        // 4. 调用Hutool 5.8.39源码存在的方法：encode(File)（返回纯Base64编码）
        String pureBase64 = Base64.encode(imageFile);

        // 5. 手动拼接数据头（替代原stripPrefix的反向操作）
        return BASE64_IMAGE_PREFIX + fileExt + BASE64_SUFFIX + pureBase64;
    }

    /**
     * 本地图片转Base64（不带数据头，纯编码字符串）
     *
     * @param imageFilePath 本地图片绝对路径
     * @return 纯Base64编码（无 data:image/xxx;base64, 前缀）
     * @throws IOException 图片读取失败时抛出
     */
    public static String imageToBase64WithoutHeader(String imageFilePath) throws IOException {
        // 直接调用Base64.encode(File)，返回纯编码
        File imageFile = FileUtil.file(imageFilePath);
        if (!imageFile.exists() || !imageFile.isFile()) {
            throw new IOException("图片文件不存在或无效：" + imageFilePath);
        }
        return Base64.encode(imageFile);
    }

    // ------------------------------------------------------------------------
    // 功能2：Base64编码 → 图片文件（保存到本地，兼容多系统）
    // ------------------------------------------------------------------------

    /**
     * Base64转图片并保存到本地（使用默认保存路径）
     *
     * @param base64Str Base64字符串（支持带数据头或不带，例：data:image/png;base64,xxx 或 xxx）
     * @param fileName  保存的图片文件名（必须带后缀，例：test.png、demo.jpg）
     * @return 图片保存后的绝对路径（兼容当前系统格式）
     * @throws IllegalArgumentException 参数非法时抛出
     * @throws IOException              目录创建失败或文件写入失败时抛出
     */
    public static String base64ToImage(String base64Str, String fileName) throws IOException {
        return base64ToImage(base64Str, fileName, getDefaultSaveRootPath());
    }

    /**
     * Base64转图片并保存到本地（自定义保存路径）
     *
     * @param base64Str          Base64字符串（支持带/不带数据头）
     * @param fileName           保存的图片文件名（必须带后缀，例：test.png、demo.jpg）
     * @param customSaveRootPath 自定义保存根路径（例：D:/my-images 或 /Users/my-images 或 /home/my-images）
     * @return 图片保存后的绝对路径
     * @throws IllegalArgumentException 参数非法时抛出
     * @throws IOException              目录创建失败或文件写入失败时抛出
     */
    public static String base64ToImage(String base64Str, String fileName, String customSaveRootPath) throws IOException {
        // 1. 核心参数校验
        if (StrUtil.isBlank(base64Str)) {
            throw new IllegalArgumentException("Base64字符串不能为空！");
        }
        if (StrUtil.isBlank(fileName) || !fileName.contains(".")) {
            throw new IllegalArgumentException("文件名不能为空，且必须带后缀（例：test.png、demo.jpg）！");
        }
        if (StrUtil.isBlank(customSaveRootPath)) {
            throw new IllegalArgumentException("自定义保存路径不能为空！");
        }

        // 2. 手动处理Base64数据头（替代不存在的stripPrefix方法）
        String pureBase64 = removeBase64Header(base64Str);

        // 3. 创建保存目录（支持多级目录，兼容多系统）
        File saveDir = FileUtil.file(customSaveRootPath);
        if (!saveDir.exists()) {
            boolean mkdirsSuccess = saveDir.mkdirs();
            if (!mkdirsSuccess) {
                throw new IOException("创建保存目录失败：" + customSaveRootPath);
            }
        }

        // 4. 拼接最终保存路径（Hutool自动处理系统分隔符）
        File targetImageFile = FileUtil.file(saveDir, fileName);

        // 5. 调用Hutool 5.8.39源码存在的方法：decodeToFile()
        Base64.decodeToFile(pureBase64, targetImageFile);

        // 6. 返回兼容当前系统的绝对路径
        return targetImageFile.getAbsolutePath();
    }

    // ------------------------------ 辅助方法：手动去除Base64数据头 ------------------------------

    /**
     * 手动去除Base64字符串中的数据头（替代不存在的stripPrefix方法）
     * 处理逻辑：如果包含 "data:image/" 和 ";base64,"，则截取后面的纯编码部分
     *
     * @param base64Str 带数据头或不带数据头的Base64字符串
     * @return 纯Base64编码
     */
    private static String removeBase64Header(String base64Str) {
        if (StrUtil.isBlank(base64Str)) {
            return base64Str;
        }
        // 判断是否包含标准图片Base64数据头
        if (base64Str.startsWith(BASE64_IMAGE_PREFIX) && base64Str.contains(BASE64_SUFFIX)) {
            // 截取 ";base64," 后面的部分
            return base64Str.substring(base64Str.indexOf(BASE64_SUFFIX) + BASE64_SUFFIX.length());
        }
        // 不带数据头，直接返回
        return base64Str;
    }

    // ------------------------------ 辅助方法：获取默认保存路径 ------------------------------

    /**
     * 获取默认保存根路径
     * 根据不同操作系统返回对应的标准存储路径，使用Path.of()拼接
     *
     * @return 跨平台兼容的默认保存根路径（Path对象）
     */
    public static String getDefaultSaveRootPath() {
        // 1. 获取操作系统类型（转小写方便判断）
        String osName = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        Path rootPath;

        // 2. 根据不同系统确定基础根路径
        if (osName.contains("win")) {
            // Windows系统：优先使用用户文档目录（C:\Users\用户名\Documents）
            rootPath = Path.of("C:", "Coalbot", "images");
        } else if (osName.contains("mac")) {
            // macOS系统：用户文档目录（/Users/用户名/Documents）
            rootPath = Path.of("Users", "Coalbot", "images");
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            // Linux/Unix系统：用户主目录（/home/用户名）或特定数据目录
            rootPath = Path.of("home", "Coalbot", "images");
        } else {
            // 其他未知系统：使用用户主目录作为 fallback
            String userHome = System.getProperty("user.home");
            rootPath = Path.of(userHome, "Coalbot", "images");
        }
        return rootPath.toString();
    }

}
