package com.xk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xk.domain.dto.PageDTO;
import com.xk.domain.dto.YearDetailedListDto;
import com.xk.domain.vo.group.YearDetailedLisVo;
import com.xk.entity.YearData;
import com.xk.mapper.YearDataMapper;
import com.xk.service.YearDataService;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

/**
 * 年度组表(YearData)表服务实现类
 *
 * @author makejava
 * @since 2024-12-05 22:52:50
 */
@Service("yearDataService")
public class YearDataServiceImpl extends ServiceImpl<YearDataMapper, YearData> implements YearDataService {

    @Override
    public List<YearData> selectBatchyearGroupIds(List<Long> yearGroupIds) {
        //1.列表判断是否为空
        if(yearGroupIds == null || yearGroupIds.size() == 0){
            return new ArrayList<>();
        }
        //2.根据年度组id的批量查询年度数据
        LambdaQueryWrapper<YearData> queryWrapper = new LambdaQueryWrapper<YearData>()
                .in(YearData::getYearGroupId, yearGroupIds);
        List<YearData> yearDataList = this.list(queryWrapper);
        return yearDataList;
    }

}

