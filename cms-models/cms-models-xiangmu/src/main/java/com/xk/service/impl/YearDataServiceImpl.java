package com.xk.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.core.web.domain.Response;
import com.xk.domain.dto.AddYearDataDto;
import com.xk.domain.dto.PageDTO;
import com.xk.domain.query.PageQuery;
import com.xk.domain.vo.group.CollegeDataVo;
import com.xk.domain.vo.group.YearDataVo;
import com.xk.entity.Project;
import com.xk.entity.YearData;
import com.xk.entity.YearGroup;
import com.xk.mapper.YearDataMapper;
import com.xk.service.YearDataService;
import com.xk.utils.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * 年度数据
     */
    private final YearDataMapper yearDataMapper;
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
    @Transactional
    public Response addYearData(AddYearDataDto addYearDataDto) {
        //1.判断年度组id是否为空
        List<YearGroup> list = yearDataMapper.findYearGroupByIdAndStatus(addYearDataDto.getYearGroupId());
        if (list == null || list.size() == 0){
            throw new ServiceException("年度组的不存在", 400);
        }
        //2开始和结束时间校验,开始时间<结束时间
        int compare = DateUtil.compare(addYearDataDto.getBegin(), addYearDataDto.getEnd());
        if (compare >= 0){
            throw new ServiceException("需要开始时间<结束时间", 400);
        }
        //3.年度数据名字不可以在同组当中出现重复
        List<YearData> yearDataList = this.lambdaQuery()
                .eq(YearData::getYearGroupId, addYearDataDto.getYearGroupId())
                .eq(YearData::getName, addYearDataDto.getName())
                .list();
        if (yearDataList != null && yearDataList.size() > 0){
            throw new ServiceException("在年度组:"+list.get(0).getName()+"年度数据名字重复", 400);
        }
        //4.新增年度数据
        YearData yearData = BeanCopyUtils.copyBean(addYearDataDto, YearData.class);
        if(this.save(yearData)){
            return Response.success();
        }
        return Response.error();
    }

    @Override
    @Transactional
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
        /*List<Project> projectList = projectService.lambdaQuery()
                .eq(Project::getYearDataId, id)
                .last("limit 1")
                .list();*/
        List<Project> projectList = yearDataMapper.findProjectByYearDataId(id);
        if (projectList != null && projectList.size() > 0){
            throw new ServiceException("当前年度数据被项目引用，无法删除", 400);
        }
        //4.删除年度数据
        boolean b = this.removeById(id);
        return b?Response.success():Response.error();
    }

    /**
     * @param pageQuery
     * @return
     */
    @Override
    public Response<PageDTO<YearDataVo>> getYearData(PageQuery pageQuery) {
       Page<YearData> page = pageQuery.toMpPageDefaultSortByCreateTimeDesc();
       LambdaQueryWrapper queryWrapper = new LambdaQueryWrapper<YearData>();
       this.page(page,queryWrapper);

       return Response.success(PageDTO.of(page,YearDataVo.class));
    }

    /**
     * 安装id查询 年度数据
     * @param id
     * @return
     */
    @Override
    public Response<YearDataVo> getYearDataById(Long id) {
       LambdaQueryWrapper queryWrapper = new LambdaQueryWrapper<YearData>()
               .eq(YearData::getYearDataId, id);
       return Response.success(BeanCopyUtils.copyBean(getOne(queryWrapper),YearDataVo.class));
    }


}

