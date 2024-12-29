package com.xk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.common.core.web.domain.Response;
import com.xk.domain.dto.collegeListDto;
import com.xk.entity.CollegeGroup;

import java.util.List;


/**
 * 学院组表(CollegeGroup)表服务接口
 *
 * @author makejava
 * @since 2024-12-05 21:21:54
 */
public interface CollegeGroupService extends IService<CollegeGroup> {
    /**
     * 查询学院组表列表
     * @param name 学院组名称
     * @return
     */
    Response<List<collegeListDto>> collegeList(String name);

    /**
     * 根据id集查询学院组列表
     * @param ids id集
     * @return 学院组列表
     */
    List<CollegeGroup> selectByIds(List<Long> ids);
}

