package com.xk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.common.core.web.domain.Response;
import com.xk.domain.dto.AddYearDataDto;
import com.xk.domain.dto.PageDTO;
import com.xk.domain.dto.YearDetailedListDto;
import com.xk.domain.vo.group.YearDetailedLisVo;
import com.xk.entity.YearData;

import javax.validation.Valid;
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
    /**
     * 根据年度组id,新增年度组数据
     */
    Response addYearData(@Valid AddYearDataDto addYearDataDto);
    /**
     * 根据年度数据的id,删除年度数据
     * @param id 年度数的id
     * @return
     */
    Response deleteYearData(Long id);
}

