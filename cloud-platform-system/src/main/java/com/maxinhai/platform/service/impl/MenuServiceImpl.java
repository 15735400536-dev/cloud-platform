package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.MenuAddDTO;
import com.maxinhai.platform.dto.MenuEditDTO;
import com.maxinhai.platform.dto.MenuQueryDTO;
import com.maxinhai.platform.excel.MenuExcel;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.listener.MenuExcelListener;
import com.maxinhai.platform.mapper.MenuMapper;
import com.maxinhai.platform.po.Menu;
import com.maxinhai.platform.service.CommonCodeCheckService;
import com.maxinhai.platform.service.MenuService;
import com.maxinhai.platform.utils.TreeNodeUtils;
import com.maxinhai.platform.vo.MenuTreeVO;
import com.maxinhai.platform.vo.MenuVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Resource
    private MenuMapper menuMapper;
    @Resource
    private MenuExcelListener menuExcelListener;
    @Resource
    private CommonCodeCheckService commonCodeCheckService;

    @Override
    public Page<MenuVO> searchByPage(MenuQueryDTO param) {
        return menuMapper.selectJoinPage(param.getPage(), MenuVO.class,
                new MPJLambdaWrapper<Menu>()
                        .like(StrUtil.isNotBlank(param.getMenuName()), Menu::getMenuName, param.getMenuName())
                        .orderByDesc(Menu::getCreateTime));
    }

    @Override
    public MenuVO getInfo(String id) {
        return menuMapper.selectJoinOne(MenuVO.class, new MPJLambdaWrapper<Menu>().eq(Menu::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        menuMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(MenuEditDTO param) {
        Menu user = BeanUtil.toBean(param, Menu.class);
        menuMapper.updateById(user);
    }

    @Override
    public void add(MenuAddDTO param) {
        boolean unique = commonCodeCheckService.isCodeUnique(Menu.class, Menu::getMenuName, param.getMenuName());
        if (unique) {
            throw new BusinessException("菜单【" + param.getMenuName() + "】已存在!");
        }
        Menu user = BeanUtil.toBean(param, Menu.class);
        menuMapper.insert(user);
    }

    @Override
    public List<MenuTreeVO> getMenuTree() {
        // 查询启用菜单集合
        List<Menu> menuList = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                .select(Menu::getId, Menu::getParentId, Menu::getMenuName, Menu::getMenuType, Menu::getUrl,
                        Menu::getComponent, Menu::getIcon, Menu::getStatus, Menu::getIsFrame, Menu::getSort)
                .eq(Menu::getStatus, 1));
        List<MenuTreeVO> treeVOList = menuList.stream()
                .map(MenuTreeVO::convert)
                .collect(Collectors.toList());
        // 按照父级ID构建树状结构
        return TreeNodeUtils.buildTree(treeVOList, "0");
    }

    @Override
    public void importExcel(MultipartFile file) {
        try {
            // 调用EasyExcel读取文件
            EasyExcel.read(file.getInputStream(), MenuExcel.class, menuExcelListener)
                    .sheet() // 读取第一个sheet
                    .doRead(); // 执行读取操作
        } catch (IOException e) {
            log.error("Excel数据导入失败", e);
            throw new BusinessException("Excel数据导入失败：" + e.getMessage());
        }
    }
}
