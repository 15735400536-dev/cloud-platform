package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.DiaryAddDTO;
import com.maxinhai.platform.dto.DiaryEditDTO;
import com.maxinhai.platform.dto.DiaryQueryDTO;
import com.maxinhai.platform.po.Diary;
import com.maxinhai.platform.vo.DiaryVO;

public interface DiaryService extends IService<Diary> {

    Page<DiaryVO> searchByPage(DiaryQueryDTO param);

    DiaryVO getInfo(String id);

    void remove(String[] ids);

    void edit(DiaryEditDTO param);

    void add(DiaryAddDTO param);

}
