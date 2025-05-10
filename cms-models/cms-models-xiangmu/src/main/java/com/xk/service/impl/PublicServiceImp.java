package com.xk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cms.common.core.web.domain.Response;
import com.xk.domain.dto.GetGroupNameDto;
import com.xk.domain.dto.PageDTO;
import com.xk.domain.dto.YearDataDto;
import com.xk.domain.query.PageQuery;
import com.xk.domain.vo.project.ProjectListvo;
import com.xk.entity.*;
import com.xk.mapper.*;
import com.xk.service.CollegeGroupService;
import com.xk.service.PublicService;
import com.xk.service.SpecialistGroupService;
import com.xk.service.YearGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicServiceImp implements PublicService {

    final YearGroupService yearGroupService;
    final YearGroupMapper yearGroupMapper;
    final YearDataMapper yearDataMapper;
    final SpecialistGroupMapper specialistGroupMapper;
    final SpecialistGroupService specialistGroupService;
    final SpecialistDataMapper specialistDataMapper;
    final CollegeGroupMapper collegeGroupMapper;
    final CollegeGroupService collegeGroupService;
    final CollegeDataMapper collegeDataMapper;
    /**
     * @param
     * @return
     */
    @Override
    public Response<String> getGroupName(GetGroupNameDto getGroupNameDto) {
//        1,年度组
        if (getGroupNameDto.getYearId() != null){
            LambdaQueryWrapper<YearGroup> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YearGroup::getYearGroupId, getGroupNameDto.getYearId());
            YearGroup yearGroup = yearGroupService.getOne(queryWrapper);
            String name = yearGroup.getName();
            return Response.success(name);
        }
        //2，专家组
        if (getGroupNameDto.getSpecialistId() != null){
            LambdaQueryWrapper<SpecialistGroup> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SpecialistGroup::getSpecialistGroupId, getGroupNameDto.getSpecialistId());
            SpecialistGroup specialistGroup = specialistGroupService.getOne(queryWrapper);
            String name = specialistGroup.getName();
            return Response.success(name);
        }
        // 2，学院组
        if (getGroupNameDto.getCollegeId() != null){
            LambdaQueryWrapper<CollegeGroup> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(CollegeGroup::getCollegeGroupId, getGroupNameDto.getCollegeId());
            CollegeGroup collegeGroup = collegeGroupService.getOne(queryWrapper);
            String name = collegeGroup.getName();
            return Response.success(name);
        }
        return Response.error("找不到此id有的分组");
    }

    /**
     * @param getGroupNameDto
     * @return
     */
    @Override
    public Response<String> getGroupDataName(GetGroupNameDto getGroupNameDto) {
        if (getGroupNameDto.getYearId() != null){
            LambdaQueryWrapper<YearData> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YearData::getYearDataId, getGroupNameDto.getYearId());
            YearData yearGroup = yearDataMapper.selectOne(queryWrapper);
            String name = yearGroup.getName();
            return Response.success(name);
        }
        //2，专家组
        if (getGroupNameDto.getSpecialistId() != null){
            LambdaQueryWrapper<SpecialistData> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SpecialistData::getSpecialistGroupId, getGroupNameDto.getSpecialistId());
            SpecialistData specialistGroup = specialistDataMapper.selectOne(queryWrapper);
            String name = specialistGroup.getUserName();
        }
        // 2，学院组
        if (getGroupNameDto.getCollegeId() != null){
           LambdaQueryWrapper<CollegeData> queryWrapper = new LambdaQueryWrapper<>();
           queryWrapper.eq(CollegeData::getCollegeDataId,getGroupNameDto.getCollegeId());
           CollegeData collegeGroup = collegeDataMapper.selectOne(queryWrapper);
           String name = collegeGroup.getUserName();
           return Response.success(name);

        }
        return Response.error("找不到此id有的分组");
    }


}
