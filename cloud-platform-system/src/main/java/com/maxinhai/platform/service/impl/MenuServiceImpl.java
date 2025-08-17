package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.MenuAddDTO;
import com.maxinhai.platform.dto.MenuEditDTO;
import com.maxinhai.platform.dto.MenuQueryDTO;
import com.maxinhai.platform.mapper.MenuMapper;
import com.maxinhai.platform.po.Menu;
import com.maxinhai.platform.service.MenuService;
import com.maxinhai.platform.utils.TreeNodeUtils;
import com.maxinhai.platform.vo.MenuTreeVO;
import com.maxinhai.platform.vo.MenuVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Resource
    private MenuMapper menuMapper;

    @Override
    public Page<MenuVO> searchByPage(MenuQueryDTO param) {
        Page<MenuVO> pageResult = menuMapper.selectJoinPage(param.getPage(), MenuVO.class,
                new MPJLambdaWrapper<Menu>()
                        .like(StrUtil.isNotBlank(param.getMenuName()), Menu::getMenuName, param.getMenuName())
                        .orderByDesc(Menu::getCreateTime));
        return pageResult;
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
                .map(menu -> MenuTreeVO.convert(menu))
                .collect(Collectors.toList());
        // 按照父级ID构建树状结构
        List<MenuTreeVO> menuTree = TreeNodeUtils.buildTree(treeVOList, "0");
        return menuTree;
    }
}
