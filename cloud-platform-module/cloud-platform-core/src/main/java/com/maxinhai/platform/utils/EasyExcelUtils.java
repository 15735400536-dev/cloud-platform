package com.maxinhai.platform.utils;

import com.alibaba.excel.EasyExcel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

public class EasyExcelUtils {

    /**
     * 到处Execl
     * @param response
     * @param fileName
     * @param sheetName
     * @param head
     * @param data
     * @throws IOException
     */
    public static void export(HttpServletResponse response,
                              String fileName,
                              String sheetName,
                              Class<?> head,
                              List<?> data) throws IOException {

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encodedFileName + ".xlsx");

        // 写入Excel
        EasyExcel.write(response.getOutputStream(), head)
                .sheet(sheetName)
                .doWrite(data);
    }

}
