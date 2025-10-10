package com.maxinhai.platform.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.maxinhai.platform.excel.MenuExcel;
import com.maxinhai.platform.po.Menu;
import com.maxinhai.platform.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Excel导入监听器，用于处理读取到的Excel数据
 */
@Slf4j
@Component
public class MenuExcelListener implements ReadListener<MenuExcel> {

    @Lazy
    @Resource
    private MenuService menuService;

    // 批量处理阈值，达到该数量就进行一次处理
    private static final int BATCH_COUNT = 100;

    // 存储读取到的数据
    private List<MenuExcel> dataList = new ArrayList<>(BATCH_COUNT);

    /**
     * 每读取一行数据就会调用该方法
     */
    @Override
    public void invoke(MenuExcel data, AnalysisContext context) {
        log.info("读取到数据: {}", data);
        dataList.add(data);

        // 达到BATCH_COUNT，需要去存储一次数据库，防止数据量太大，内存溢出
        if (dataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            dataList.clear();
        }
    }

    /**
     * 所有数据读取完成后调用该方法
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也被存储
        saveData();
        // 存储完成清理 list
        dataList.clear();
        log.info("所有数据解析完成！");
    }

    /**
     * 保存数据到数据库
     */
    private void saveData() {
        log.info("开始保存 {} 条数据到数据库", dataList.size());
        // 没有内容不执行后面操作
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        // 保存数据
        dataList = dataList.stream()
                .sorted(Comparator.comparing(MenuExcel::getParentKey))
                .collect(Collectors.toList());
        // 菜单标识 -> 菜单
        Map<String, String> keyIdMap = new HashMap<>();
        // 菜单集合
        List<Menu> menuList = new ArrayList<>();
        for (MenuExcel menuExcel : dataList) {
            Menu menu = MenuExcel.build(menuExcel);
            menu.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            menu.setParentId(keyIdMap.getOrDefault(menuExcel.getKey(), "0"));
            menuList.add(menu);
            keyIdMap.put(menuExcel.getKey(), menu.getId());
        }
        menuService.saveBatch(menuList);
        log.info("数据保存完成！");
    }

}
