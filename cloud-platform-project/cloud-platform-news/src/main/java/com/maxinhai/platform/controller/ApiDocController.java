package com.maxinhai.platform.controller;

import com.maxinhai.platform.utils.AjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apiDoc")
@Tag(name = "演示controller", description = "演示controller")
public class ApiDocController {

    @GetMapping("/index")
    @Operation(summary = "演示方法", description = "演示方法的注释")
    public AjaxResult<String> index(@Parameter(description = "参数1", required = true) String param) {
        return AjaxResult.success("请求成功");
    }

    @GetMapping("/get")
    public AjaxResult<Void> get() {
        return AjaxResult.success();
    }

    @PostMapping("/post")
    public AjaxResult<Void> post() {
        return AjaxResult.success();
    }

    @DeleteMapping("/delete")
    public AjaxResult<Void> delete() {
        return AjaxResult.success();
    }

    @PutMapping("/put")
    public AjaxResult<Void> put() {
        return AjaxResult.success();
    }

}
