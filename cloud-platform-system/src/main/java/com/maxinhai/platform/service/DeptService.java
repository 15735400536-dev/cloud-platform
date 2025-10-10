package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.*;
import com.maxinhai.platform.po.Dept;
import com.maxinhai.platform.vo.DeptTreeVO;
import com.maxinhai.platform.vo.DeptVO;
import com.maxinhai.platform.vo.UserVO;

import java.util.List;

public interface DeptService extends IService<Dept> {

    Page<DeptVO> searchByPage(DeptQueryDTO param);

    DeptVO getInfo(String id);

    void remove(String[] ids);

    void edit(DeptEditDTO param);

    void add(DeptAddDTO param);

    /**
     * 部门绑定用户
     * @param param
     */
    void binding(DeptUserDTO param);

    List<DeptTreeVO> getTree();

    Page<UserVO> searchUserByPage(DeptUserQueryDTO param);

}
