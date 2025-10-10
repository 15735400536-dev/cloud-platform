package com.maxinhai.platform.utils;

import com.maxinhai.platform.enums.FileType;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName：FileTypeUtils
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 14:09
 * @Description: 文件类型判断工具类
 */
public class FileTypeUtils {

    // 定义支持的文件扩展名
    private static final Set<String> WORD_EXTENSIONS = new HashSet<>(Arrays.asList(
            "doc", "docx", "dot", "dotx", "docm"
    ));

    private static final Set<String> EXCEL_EXTENSIONS = new HashSet<>(Arrays.asList(
            "xls", "xlsx", "xlsm", "xlsb", "xltx", "xltm"
    ));

    private static final Set<String> PDF_EXTENSIONS = new HashSet<>(Arrays.asList(
            "pdf"
    ));

    private static final Set<String> IMAGE_EXTENSIONS = new HashSet<>(Arrays.asList(
            "jpg", "jpeg", "png", "gif", "bmp", "webp", "svg", "tiff"
    ));

    private static final Set<String> VIDEO_EXTENSIONS = new HashSet<>(Arrays.asList(
            "mp4", "avi", "mov", "wmv", "flv", "mkv", "mpeg", "mpg"
    ));

    private static final Set<String> TEXT_EXTENSIONS = new HashSet<>(Arrays.asList(
            "txt", "md", "csv", "log", "xml", "json", "html", "htm", "css", "js"
    ));

    /**
     * 根据文件名获取文件类型
     */
    public static FileType getFileType(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return FileType.UNKNOWN;
        }

        // 获取文件扩展名
        String extension = StringUtils.getFilenameExtension(fileName).toLowerCase();
        if (extension == null || extension.isEmpty()) {
            return FileType.UNKNOWN;
        }

        // 判断文件类型
        if (WORD_EXTENSIONS.contains(extension)) {
            return FileType.WORD;
        } else if (EXCEL_EXTENSIONS.contains(extension)) {
            return FileType.EXCEL;
        } else if (PDF_EXTENSIONS.contains(extension)) {
            return FileType.PDF;
        } else if (IMAGE_EXTENSIONS.contains(extension)) {
            return FileType.IMAGE;
        } else if (VIDEO_EXTENSIONS.contains(extension)) {
            return FileType.VIDEO;
        } else if (TEXT_EXTENSIONS.contains(extension)) {
            return FileType.TEXT;
        } else {
            return FileType.UNKNOWN;
        }
    }

    /**
     * 获取文件类型描述
     */
    public static String getFileTypeDescription(String fileName) {
        return getFileType(fileName).getDescription();
    }

}
