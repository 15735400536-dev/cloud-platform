package com.maxinhai.platform.controller;

import com.maxinhai.platform.constants.FileConstants;
import com.maxinhai.platform.dto.UploadFileQueryDTO;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.mapper.UploadFileMapper;
import com.maxinhai.platform.po.UploadFile;
import com.maxinhai.platform.service.UploadFileService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.UploadFileVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

/**
 * @ClassName：UploadFileController
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 14:20
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@RestController
@RequestMapping("/file")
@Api(tags = "上传文件接口")
public class UploadFileController {

    @Resource
    private UploadFileMapper uploadFileMapper;
    @Resource
    private UploadFileService uploadFileService;
    private final Path fileStorageLocation;

    {
        fileStorageLocation = Paths.get(FileConstants.UPLOAD_DIR).toAbsolutePath().normalize();
        try {
            // 创建上传目录（如果不存在）
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new BusinessException("无法创建文件上传目录", ex);
        }
    }

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询上传文件信息", notes = "根据查询条件分页查询上传文件信息")
    public AjaxResult<PageResult<UploadFileVO>> searchByPage(@RequestBody UploadFileQueryDTO param) {
        return AjaxResult.success(PageResult.convert(uploadFileService.searchByPage(param)));
    }

    @PostMapping("/uploadFile")
    @ApiOperation(value = "批量文件上传", notes = "批量文件上传")
    public AjaxResult<String> uploadFile(List<MultipartFile> files) {
        for (MultipartFile file : files) {
            // 清理文件名，防止路径遍历攻击
            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
            // 检查文件名是否包含无效字符
            if (originalFileName.contains("..")) {
                throw new BusinessException("文件名包含无效路径序列: " + originalFileName);
            }
            try {
                // 生成唯一文件名，避免覆盖
                String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName;
                Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);
                // 保存文件到目标位置
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                // 创建文件信息记录并保存到数据库
                UploadFile uploadFile = new UploadFile(
                        originalFileName,
                        file.getContentType(),
                        file.getSize(),
                        FileConstants.UPLOAD_DIR + uniqueFileName,
                        FileConstants.UPLOAD_ACCESS_URL + uniqueFileName,
                        new Date()
                );

                uploadFileMapper.insert(uploadFile);
            } catch (IOException e) {
                throw new BusinessException("存储文件失败:" + originalFileName, e);
            }
        }
        return AjaxResult.success();
    }

    @GetMapping("/remove/{id}")
    @ApiOperation(value = "删除文件", notes = "根据ID删除文件")
    public AjaxResult<String> remove(@PathVariable("id") String id) {
        uploadFileService.delFile(id);
        return AjaxResult.success();
    }

}
