package com.maxinhai.platform.utils;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Slf4j
@Component
public class DocxUtils {

    @Autowired
    private ResourceLoader resourceLoader;
    @Value("${docx.template.path:classpath:/templates/}")
    private String templatePath;


    /**
     * 根据模板和数据生成docx文件
     * @param templateName 模板文件名
     * @param dataMap 包含键值对的Map
     * @return 生成的docx字节数组
     * @throws IOException IO异常
     */
    public byte[] generateDocx(String templateName, Map<String, String> dataMap) throws IOException {
        // 加载模板文件
        Resource resource = resourceLoader.getResource(templatePath + templateName);
        try (InputStream is = resource.getInputStream();
             XWPFDocument document = new XWPFDocument(is);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // 处理段落中的占位符
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                String text = paragraph.getText();

                // 如果段落包含占位符，则进行替换
                if (StrUtil.contains(text, "${") && StrUtil.contains(text, "}")) {
                    // 清除原有内容
                    for (int i = paragraph.getRuns().size() - 1; i >= 0; i--) {
                        paragraph.removeRun(i);
                    }

                    // 替换占位符后添加新内容
                    String replacedText = replacePlaceholders(text, dataMap);
                    XWPFRun run = paragraph.createRun();
                    run.setText(replacedText);
                    // 可以设置字体等样式
                    run.setFontFamily("SimSun"); // 宋体
                }
            }

            // 处理表格中的占位符
            document.getTables().forEach(table -> {
                table.getRows().forEach(row -> {
                    row.getTableCells().forEach(cell -> {
                        cell.getParagraphs().forEach(paragraph -> {
                            String text = paragraph.getText();
                            if (StrUtil.contains(text, "${") && StrUtil.contains(text, "}")) {
                                for (int i = paragraph.getRuns().size() - 1; i >= 0; i--) {
                                    paragraph.removeRun(i);
                                }
                                String replacedText = replacePlaceholders(text, dataMap);
                                XWPFRun run = paragraph.createRun();
                                run.setText(replacedText);
                                run.setFontFamily("SimSun");
                            }
                        });
                    });
                });
            });

            // 写入输出流
            document.write(out);
            return out.toByteArray();
        }
    }

    /**
     * 替换文本中的占位符
     * @param text 原始文本
     * @param dataMap 数据映射
     * @return 替换后的文本
     */
    private String replacePlaceholders(String text, Map<String, String> dataMap) {
        String result = text;
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            String value = entry.getValue() != null ? entry.getValue() : "";
            result = StrUtil.replace(result, placeholder, value);
        }
        return result;
    }

}
