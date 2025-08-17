package com.maxinhai.platform.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxinhai.platform.dto.InspectionItemAddDTO;
import com.maxinhai.platform.dto.InspectionItemEditDTO;
import com.maxinhai.platform.dto.InspectionItemQueryDTO;
import com.maxinhai.platform.mapper.InspectionItemMapper;
import com.maxinhai.platform.po.InspectionItem;
import com.maxinhai.platform.service.InspectionItemService;
import com.maxinhai.platform.vo.InspectionItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InspectionItemServiceImpl extends ServiceImpl<InspectionItemMapper, InspectionItem> implements InspectionItemService {
    @Override
    public Page<InspectionItemVO> searchByPage(InspectionItemQueryDTO param) {
        return null;
    }

    @Override
    public InspectionItemVO getInfo(String id) {
        return null;
    }

    @Override
    public void remove(String[] ids) {

    }

    @Override
    public void edit(InspectionItemEditDTO param) {

    }

    @Override
    public void add(InspectionItemAddDTO param) {

    }
}
