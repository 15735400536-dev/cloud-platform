package com.maxinhai.platform.controller;

import com.maxinhai.platform.bo.ApiParam;
import com.maxinhai.platform.dto.CustomSqlAddDTO;
import com.maxinhai.platform.dto.CustomViewAddDTO;
import com.maxinhai.platform.dto.CustomViewEditDTO;
import com.maxinhai.platform.dto.CustomViewQueryDTO;
import com.maxinhai.platform.enums.ApiParamType;
import com.maxinhai.platform.handler.ApiHandler;
import com.maxinhai.platform.service.CustomViewService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.CustomViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName：ConditionController
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 18:40
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Slf4j
@RestController
@RequestMapping("/view")
@Api(tags = "视图管理接口")
public class ViewController {

    @Resource
    private CustomViewService viewService;
    @Resource
    private ApiHandler apiHandler;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询视图信息", notes = "根据视图分页查询视图信息")
    public AjaxResult<PageResult<CustomViewVO>> searchByPage(@RequestBody CustomViewQueryDTO param) {
        return AjaxResult.success(PageResult.convert(viewService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取视图信息", notes = "根据视图ID获取详细信息")
    public AjaxResult<CustomViewVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(viewService.getInfo(id));
    }

    @PostMapping("/addCustomView")
    @ApiOperation(value = "添加视图信息", notes = "添加视图信息")
    public AjaxResult<Void> addCustomView(@RequestBody CustomViewAddDTO param) {
        viewService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editCustomView")
    @ApiOperation(value = "编辑视图信息", notes = "根据视图ID编辑视图信息")
    public AjaxResult<Void> editCustomView(@RequestBody CustomViewEditDTO param) {
        viewService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeCustomView")
    @ApiOperation(value = "删除视图信息", notes = "根据视图ID数组删除视图信息")
    public AjaxResult<Void> removeCustomView(@RequestBody String[] ids) {
        viewService.remove(ids);
        return AjaxResult.success();
    }

    @DeleteMapping("/apiDelete/{id}")
    @ApiOperation(value = "DELETE请求", notes = "DELETE请求")
    public AjaxResult<String> apiDelete(@PathVariable("id") String id) {
        return AjaxResult.success("id:" + id);
    }

    @PutMapping("/apiPut")
    @ApiOperation(value = "PUT请求", notes = "PUT请求")
    public AjaxResult<CustomSqlAddDTO> apiPut(@RequestBody CustomSqlAddDTO dto) {
        return AjaxResult.success(dto);
    }

    @PostMapping("/apiPost")
    @ApiOperation(value = "POST请求-参数JSON", notes = "POST请求-参数JSON")
    public AjaxResult<Map<String, Object>> apiPost(@RequestBody Map<String, Object> params) {
        return AjaxResult.success(params);
    }

    @PostMapping("/apiPostWithParams")
    @ApiOperation(value = "POST请求-参数JSON", notes = "POST请求-参数JSON")
    public AjaxResult<CustomSqlAddDTO> apiPostWithParams(@RequestBody CustomSqlAddDTO dto) {
        return AjaxResult.success(dto);
    }

    @PostMapping("/apiWithFormData")
    @ApiOperation(value = "POST请求-参数FORM_DATA", notes = "POST请求-参数FORM_DATA")
    public AjaxResult<Map<String, Object>> apiWithFormData(MultipartFile file, @RequestParam Map<String, Object> params) {
        log.debug("apiWithFormData -> 接收参数： {}", params);
        params.put("file", Objects.isNull(file) ? null : file.getOriginalFilename());
        return AjaxResult.success(params);
    }

    @PostMapping("/apiPostWithFormData")
    @ApiOperation(value = "POST请求-参数FORM_DATA", notes = "POST请求-参数FORM_DATA")
    public AjaxResult<Map<String, Object>> apiPostWithFormData(MultipartFile file, CustomSqlAddDTO dto) {
        log.debug("apiPostWithFormData -> 接收参数： {}", dto);
        Map<String, Object> params = new HashMap<>();
        params.put("file", Objects.isNull(file) ? null : file.getOriginalFilename());
        params.put("dto", dto);
        return AjaxResult.success(params);
    }

    @PostMapping("/apiPostWithFormDatas")
    @ApiOperation(value = "POST请求-参数FORM_DATA", notes = "POST请求-参数FORM_DATA")
    public AjaxResult<Map<String, Object>> apiPostWithFormDatas(List<MultipartFile> files, CustomSqlAddDTO dto) {
        log.debug("apiPostWithFormDatas -> 接收参数： {}", dto);
        Map<String, Object> params = new HashMap<>();
        params.put("files", CollectionUtils.isEmpty(files) ? null : files.stream().map(MultipartFile::getOriginalFilename).collect(Collectors.toList()));
        params.put("dto", dto);
        return AjaxResult.success(params);
    }

    @PostMapping("/uploadFile")
    @ApiOperation(value = "POST请求-上传文件", notes = "POST请求-上传文件")
    public AjaxResult<Map<String, Object>> uploadFile(List<MultipartFile> files) {
        Map<String, Object> params = new HashMap<>();
        params.put("files", CollectionUtils.isEmpty(files) ? null : files.stream().map(MultipartFile::getOriginalFilename).collect(Collectors.toList()));
        return AjaxResult.success(params);
    }

    @GetMapping("/apiWithPathVariable/{name}")
    @ApiOperation(value = "GET请求-参数PathVariable", notes = "GET请求-参数PathVariable")
    public AjaxResult<String> apiWithPathVariable(@PathVariable("name") String name) {
        return AjaxResult.success(name);
    }

    @GetMapping("/apiGet")
    @ApiOperation(value = "GET请求-参数RequestParam", notes = "GET请求-参数RequestParam")
    public AjaxResult<String> apiGet(@RequestParam("name") String name) {
        return AjaxResult.success(name);
    }

    @GetMapping("/api")
    public AjaxResult<String> api() throws IOException {
        String result = apiHandler.execute("/view/apiDelete", List.of(
                new ApiParam(ApiParamType.STRING, "id", "20251116")
        ));
        log.info("callApiDelete: {}", result);

        result = apiHandler.execute("/view/apiPut", List.of(
                new ApiParam(ApiParamType.STRING, "name", "test"),
                new ApiParam(ApiParamType.STRING, "sql", "select * from test")
        ));
        log.info("callApiPut: {}", result);

        result = apiHandler.execute("/view/apiPost", List.of(
                new ApiParam(ApiParamType.STRING, "username", "admin"),
                new ApiParam(ApiParamType.NUMBER, "age", 25)
        ));
        log.info("callApiPost: {}", result);

        result = apiHandler.execute("/view/apiPostWithParams", List.of(
                new ApiParam(ApiParamType.STRING, "id", "2001"),
                new ApiParam(ApiParamType.STRING, "content", "test content")
        ));
        log.info("callApiPostWithParams: {}", result);

        result = apiHandler.execute("/view/apiPostWithFormData", List.of(
                new ApiParam(ApiParamType.STRING, "type", "uploadFile"),
                new ApiParam(ApiParamType.NUMBER, "size", 1024),
                new ApiParam(ApiParamType.FILE, "image", "C:\\Users\\MaXinHai\\Pictures\\v2-e43ede5c4b1cfef59ba6fbf22843640e_1440w.jpg")
        ));
        log.info("apiPostWithFormData: {}", result);

        result = apiHandler.execute("/view/apiPostWithFormData", List.of(
                new ApiParam(ApiParamType.STRING, "sql", "select * from sys_user"),
                new ApiParam(ApiParamType.NUMBER, "dataSourceId", "mysql"),
                new ApiParam(ApiParamType.FILE, "image", "C:\\Users\\MaXinHai\\Pictures\\v2-e43ede5c4b1cfef59ba6fbf22843640e_1440w.jpg")
        ));
        log.info("callApiPostWithFormData: {}", result);

        result = apiHandler.execute("/view/apiWithPathVariable", List.of(
                new ApiParam(ApiParamType.STRING, "name", "MaXinHai")
        ));
        log.info("callApiWithPathVariable: {}", result);

        result = apiHandler.execute("/view/apiGet", List.of(
                new ApiParam(ApiParamType.STRING, "name", "李四")
        ));
        log.info("callapiGet: {}", result);

        result = apiHandler.execute("/view/apiPostWithFormDatas", List.of(
                new ApiParam(ApiParamType.STRING, "sql", "select * from sys_test"),
                new ApiParam(ApiParamType.NUMBER, "dataSourceId", "pgsql"),
                new ApiParam(ApiParamType.FILE, "files", "C:\\Users\\MaXinHai\\Pictures\\v2-e43ede5c4b1cfef59ba6fbf22843640e_1440w.jpg"),
                new ApiParam(ApiParamType.FILE, "files", "C:\\Users\\MaXinHai\\Pictures\\v2-e8dffee7fd205a33e2f53c4905568a35_1440w.jpg"),
                new ApiParam(ApiParamType.FILE, "files", "C:\\Users\\MaXinHai\\Pictures\\v2-af3561f32c2746f9e01e22469070737d_1440w.jpg")
        ));
        log.info("apiPostWithFormDatas: {}", result);

        result = apiHandler.execute("/view/uploadFile", List.of(
                new ApiParam(ApiParamType.FILE, "files", "C:\\Users\\MaXinHai\\Pictures\\v2-e43ede5c4b1cfef59ba6fbf22843640e_1440w.jpg"),
                new ApiParam(ApiParamType.FILE, "files", "C:\\Users\\MaXinHai\\Pictures\\v2-e8dffee7fd205a33e2f53c4905568a35_1440w.jpg"),
                new ApiParam(ApiParamType.FILE, "files", "C:\\Users\\MaXinHai\\Pictures\\v2-af3561f32c2746f9e01e22469070737d_1440w.jpg")
        ));
        log.info("uploadFile: {}", result);

        return AjaxResult.success();
    }

}
