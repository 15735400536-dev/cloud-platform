package com.maxinhai.platform.service.impl;

import com.maxinhai.platform.po.Demo;
import com.maxinhai.platform.service.DemoService;
import org.springframework.stereotype.Service;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName：DemoServiceImpl
 * @Author: XinHai.Ma
 * @Date: 2025/8/28 11:16
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Service
@WebService(
        serviceName = "DemoService",
        targetNamespace = "http://service.demo.maxinhai.com",
        endpointInterface = "com.maxinhai.platform.service.DemoService"
)
public class DemoServiceImpl implements DemoService {

    private static final List<Demo> dataList = new ArrayList<>();

    @Override
    public List<Demo> getDemoList() {
        return dataList;
    }

    @Override
    public Demo getDemoById(String id) {
        return dataList.stream().filter(demo -> Objects.nonNull(id) && id.length() > 0 && id.equals(demo.getId())).findFirst().get();
    }

    @Override
    public boolean addDemo(Demo demo) {
        dataList.add(demo);
        return true;
    }

    @Override
    public boolean editDemo(Demo demo) {
        for (int i = 0; i < dataList.size(); i++) {
            Demo oldItem = dataList.get(i);
            if (oldItem.getId().equals(demo.getId())) {
                // 替换元素
                dataList.set(i, demo);
                return true;
            }
        }
        return false;
    }
}
