package com.maxinhai.platform.service;

import com.maxinhai.platform.po.Demo;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import java.util.List;

/**
 * 定义WebService服务
 */
@WebService(name = "DemoService", targetNamespace = "http://service.demo.maxinhai.com")
public interface DemoService {

    @WebMethod(operationName = "getDemoList")
    @WebResult(name = "DemoResult")
    List<Demo> getDemoList();

    @WebMethod(operationName = "getDemoById")
    @WebResult(name = "DemoResult")
    Demo getDemoById(String id);

    @WebMethod(operationName = "addDemo")
    @WebResult(name = "UserResult")
    boolean addDemo(Demo demo);

    @WebMethod(operationName = "editDemo")
    @WebResult(name = "UserResult")
    boolean editDemo(Demo demo);

}
