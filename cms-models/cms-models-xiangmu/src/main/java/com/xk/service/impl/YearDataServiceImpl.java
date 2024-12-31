package com.xk.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.core.utils.DateUtils;
import com.cms.common.core.web.domain.Response;
import com.xk.domain.dto.AddYearDataDto;
import com.xk.domain.dto.PageDTO;
import com.xk.domain.dto.YearDetailedListDto;
import com.xk.domain.vo.group.YearDetailedLisVo;
import com.xk.entity.Project;
import com.xk.entity.YearData;
import com.xk.entity.YearGroup;
import com.xk.mapper.YearDataMapper;
import com.xk.service.ProjectService;
import com.xk.service.YearDataService;
import com.xk.service.YearGroupService;
import com.xk.utils.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.rmi.ServerError;
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
@RequiredArgsConstructor
public class YearDataServiceImpl extends ServiceImpl<YearDataMapper, YearData> implements YearDataService {
    /**
     * 年度组的服务
     */
    private final YearGroupService yearGroupService;
    /**
     * 项目的服务
     */
    private final ProjectService projectService;
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

    @Override
    public Response addYearData(AddYearDataDto addYearDataDto) {
        //1.判断年度组id是否为空
        List<YearGroup> list = yearGroupService.lambdaQuery()
                .eq(YearGroup::getYearGroupId, addYearDataDto.getYearGroupId())
                .eq(YearGroup::getStatus, 0)
                .last("limit 1")
                .list();
        if (list == null || list.size() == 0){
            throw new ServiceException("年度组的不存在", 400);
        }
        //2开始和结束时间校验,开始时间<结束时间
        int compare = DateUtil.compare(addYearDataDto.getBegin(), addYearDataDto.getEnd());
        if (compare >= 0){
            throw new ServiceException("需要开始时间<解释时间", 400);
        }
        //3.新增年度数据
        YearData yearData = BeanCopyUtils.copyBean(addYearDataDto, YearData.class);
        if(this.save(yearData)){
            return Response.success();
        }
        return Response.error();
    }

    @Override
    public Response deleteYearData(Long id) {
        //1.先判断是否存在
        List<YearData> list = this.lambdaQuery()
                .eq(YearData::getYearDataId, id)
                .last("limit 1")
                .list();
        if (list == null || list.size() == 0){
            throw new ServiceException("年度数据不存在", 400);
        }
        //2.判断有没有项目引用这个年度数据否无法删除
        List<Project> projectList = projectService.lambdaQuery()
                .eq(Project::getYearDataId, id)
                .last("limit 1")
                .list();
        if (projectList != null && projectList.size() > 0){
            throw new ServiceException("当前年度数据被项目引用，无法删除", 400);
        }
        //3.删除年度数据
        boolean b = this.removeById(id);
        return b?Response.success():Response.error();
    }

}

