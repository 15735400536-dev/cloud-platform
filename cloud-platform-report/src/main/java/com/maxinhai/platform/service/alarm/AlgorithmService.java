package com.maxinhai.platform.service.alarm;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.alarm.AlgorithmAddDTO;
import com.maxinhai.platform.dto.alarm.AlgorithmEditDTO;
import com.maxinhai.platform.dto.alarm.AlgorithmQueryDTO;
import com.maxinhai.platform.po.alarm.Algorithm;
import com.maxinhai.platform.vo.alarm.AlgorithmVO;

public interface AlgorithmService extends IService<Algorithm> {

    Page<AlgorithmVO> searchByPage(AlgorithmQueryDTO param);

    AlgorithmVO getInfo(String id);

    void remove(String[] ids);

    void edit(AlgorithmEditDTO param);

    void add(AlgorithmAddDTO param);

}
