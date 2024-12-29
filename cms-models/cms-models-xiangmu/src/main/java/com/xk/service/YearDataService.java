package com.xk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xk.entity.YearData;

import java.util.List;


/**
 * 年度组表(YearData)表服务接口
 *
 * @author makejava
 * @since 2024-12-05 22:52:49
 */
public interface YearDataService extends IService<YearData> {
    /**
     * 根据年度组的id列表查询年度数据
     * @param yearGroupIds 年度组id列表
     * @return 年度数据
     */
    List<YearData> selectBatchyearGroupIds(List<Long> yearGroupIds);


}

