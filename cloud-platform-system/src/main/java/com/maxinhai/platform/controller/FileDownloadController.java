package com.maxinhai.platform.controller;

import com.maxinhai.platform.utils.FileDownloadUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/file/download")
@Api(tags = "文件下载接口")
public class FileDownloadController {

    // 下载resources内PDF
    @GetMapping("/pdf/res")
    @ApiOperation(value = "下载resources内PDF", notes = "下载resources内PDF")
    public void downloadResPdf(HttpServletResponse response) throws IOException {
        String path = "C:\\Users\\MaXinHai\\Downloads\\人是怎么变强的？ - 知乎.pdf";
        FileDownloadUtils.downloadPdfFromServer(response, path, "资料文档.pdf");
    }

    // 下载服务器路径Word
    @GetMapping("/word/server")
    @ApiOperation(value = "下载服务器路径Word", notes = "下载服务器路径Word")
    public void downloadServerWord(HttpServletResponse response) throws IOException {
        String path = "C:\\Users\\MaXinHai\\Downloads\\template.docx";
        FileDownloadUtils.downloadWordFromServer(response, path, "工作报告.docx");
    }

    // 下载服务器图片
    @GetMapping("/img/server")
    @ApiOperation(value = "下载服务器图片", notes = "下载服务器图片")
    public void downloadServerImg(HttpServletResponse response) throws IOException {
        String path = "C:\\Users\\MaXinHai\\Downloads\\【哲风壁纸】夜店女孩-潮女.png";
        FileDownloadUtils.downloadImgFromServer(response, path, "图片.png", FileDownloadUtils.CONTENT_PNG);
    }

    // 原有Excel下载接口不变
    @GetMapping("/excel/res")
    @ApiOperation(value = "下载resources内Excel", notes = "下载resources内Excel")
    public void downloadResExcel(HttpServletResponse response) throws IOException {
        FileDownloadUtils.downloadExcelFromResource(response, "user.xlsx", "导入模板.xlsx");
    }

    @GetMapping("/json/res")
    @ApiOperation(value = "下载resources内json", notes = "下载resources内json")
    public void downloadJson(HttpServletResponse response) throws IOException {
        // resources/user.json
        FileDownloadUtils.downloadJsonFromResource(response, "user.json", "配置模板.json");
    }

    @GetMapping("/json/server")
    @ApiOperation(value = "下载服务器json", notes = "下载服务器json")
    public void downloadServerJson(HttpServletResponse response) throws IOException {
        // Windows
        String path = "C:\\JavaProject\\cloud-platform\\cloud-platform-system\\src\\main\\resources\\user.json";
        // Linux
        // String path = "/home/app/files/data.json";
        FileDownloadUtils.downloadJsonFromServer(response, path, "数据文件.json");
    }

}
