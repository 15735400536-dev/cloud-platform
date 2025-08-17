package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.RoleAddDTO;
import com.maxinhai.platform.dto.RoleEditDTO;
import com.maxinhai.platform.dto.RoleMenuDTO;
import com.maxinhai.platform.dto.RoleQueryDTO;
import com.maxinhai.platform.po.Role;
import com.maxinhai.platform.vo.RoleVO;

public interface RoleService extends IService<Role> {

    Page<RoleVO> searchByPage(RoleQueryDTO param);

    RoleVO getInfo(String id);

    void remove(String[] ids);

    void edit(RoleEditDTO param);

    void add(RoleAddDTO param);

    /**
     * 角色绑定菜单
     * @param param
     */
    void binding(RoleMenuDTO param);

}
