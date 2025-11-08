package com.maxinhai.platform.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.bo.UserBO;
import com.maxinhai.platform.dto.UserAddDTO;
import com.maxinhai.platform.dto.UserEditDTO;
import com.maxinhai.platform.dto.UserQueryDTO;
import com.maxinhai.platform.dto.UserRoleDTO;
import com.maxinhai.platform.excel.UserExcel;
import com.maxinhai.platform.po.User;
import com.maxinhai.platform.vo.RoleVO;
import com.maxinhai.platform.vo.UserVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {

    Page<UserVO> searchByPage(UserQueryDTO param);

    UserVO getInfo(String id);

    void remove(String[] ids);

    void edit(UserEditDTO param);

    void add(UserAddDTO param);

    /**
     * 用户绑定角色
     * @param param
     */
    void binding(UserRoleDTO param);

    List<RoleVO> getRoles(String userId);

    void importExcel(MultipartFile file);

    /**
     * 保存excel数据
     * @param dataList excel数据
     */
    void saveExcelData(List<UserExcel> dataList);

    List<Map<String, Object>> queryUserListDuplicateAccount();

    List<UserBO> getUserList();

    /**
     * 获取用户名集合，用户创建人、修改人回显
     * @return 用户MAP
     */
    Map<String, String> getUserMap();

}
