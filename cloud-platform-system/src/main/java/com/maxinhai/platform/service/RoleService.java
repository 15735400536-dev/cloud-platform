package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.RoleAddDTO;
import com.maxinhai.platform.dto.RoleEditDTO;
import com.maxinhai.platform.dto.RoleMenuDTO;
import com.maxinhai.platform.dto.RoleQueryDTO;
import com.maxinhai.platform.po.Role;
import com.maxinhai.platform.vo.RoleVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RoleService extends IService<Role> {

    Page<RoleVO> searchByPage(RoleQueryDTO param);

    RoleVO getInfo(String id);

    void remove(String[] ids);

    Role edit(RoleEditDTO param);

    Role add(RoleAddDTO param);

    List<Role> getRoleList();

    /**
     * 角色绑定菜单
     * @param param
     */
    void binding(RoleMenuDTO param);

    void importExcel(MultipartFile file);

}
