package com.xk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xk.entity.CollegeData;

import java.util.List;


/**
 * 学院组人员表(CollegeData)表服务接口
 *
 * @author YaWu
 * @since 2024-12-19 00:56:46
 */
public interface CollegeDataService extends IService<CollegeData> {
    /**
     * 获取当前用户的学院组id集合
     * @return 学院组id集合
     */
    List<Long> getCollegeIdByUserId();

}

