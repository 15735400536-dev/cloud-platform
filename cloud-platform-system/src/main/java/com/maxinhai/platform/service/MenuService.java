package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.MenuAddDTO;
import com.maxinhai.platform.dto.MenuEditDTO;
import com.maxinhai.platform.dto.MenuQueryDTO;
import com.maxinhai.platform.po.Menu;
import com.maxinhai.platform.vo.MenuTreeVO;
import com.maxinhai.platform.vo.MenuVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MenuService extends IService<Menu> {

    Page<MenuVO> searchByPage(MenuQueryDTO page);

    MenuVO getInfo(String id);

    void remove(String[] ids);

    void edit(MenuEditDTO Menu);

    void add(MenuAddDTO Menu);

    /**
     * 获取菜单树状结构
     * @return
     */
    List<MenuTreeVO> getMenuTree();

    void importExcel(MultipartFile file);

}
