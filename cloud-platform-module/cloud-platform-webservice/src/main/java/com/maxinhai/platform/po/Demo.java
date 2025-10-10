package com.maxinhai.platform.po;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @ClassName：Demo
 * @Author: XinHai.Ma
 * @Date: 2025/8/28 11:11
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
// 声明为XML根元素
@XmlRootElement(name = "Demo")
public class Demo {

    private String id;
    private String code;
    private String name;

    public Demo() {
    }

    public Demo(String id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Demo{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
