package com.xk.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.core.web.domain.Response;
import com.xk.domain.dto.*;
import com.xk.domain.vo.group.CollegeDataVo;
import com.xk.domain.vo.group.CollegeDetailedLisVo;
import com.xk.domain.vo.group.CollegeGroupVo;
import com.xk.entity.CollegeData;
import com.xk.entity.CollegeGroup;
import com.xk.entity.User;
import com.xk.mapper.CollegeGroupMapper;
import com.xk.service.CollegeDataService;
import com.xk.service.CollegeGroupService;
import com.xk.service.UserService;
import com.xk.utils.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 学院组表(CollegeGroup)表服务实现类
 *
 * @author yawu
 * @since 2024-12-05 21:21:54
 */
@Service
@RequiredArgsConstructor
public class CollegeGroupServiceImpl extends ServiceImpl<CollegeGroupMapper, CollegeGroup> implements CollegeGroupService {
    /**
     * 学院人员组
     */
    private final CollegeDataService collegeDataService;
    /**
     * 用户服务
     */
    private final UserService userService;


    @Override
    public Response<List<collegeListDto>> collegeList(String name) {
        //1构造条件
        LambdaQueryWrapper<CollegeGroup> wrapper = new LambdaQueryWrapper<CollegeGroup>()
                .eq(CollegeGroup::getStatus, 0)
                .like(name != null && name.length() > 0, CollegeGroup::getName, name)
                .orderByAsc(CollegeGroup::getCreateTime);
        //2.查询
        List<CollegeGroup> collegeGroups = this.list(wrapper);
        List<collegeListDto> listDtos = BeanCopyUtils.copyBeans(collegeGroups, collegeListDto.class);
        return Response.success(listDtos);
    }

    @Override
    public List<CollegeGroup> selectByIds(List<Long> ids) {
        //1.去重
        List<Long> list = ids.stream()
                .distinct()
                .collect(Collectors.toList());
        // 检查列表是否为空
        if (list.isEmpty()) {
            return Collections.emptyList(); // 返回空结果
        }
        //2.构建条件
        List<CollegeGroup> groupList = this.lambdaQuery()
                .in(CollegeGroup::getCollegeGroupId, list)
                .list();
        return groupList;
    }

    @Override
    public Response<CollegeGroupVo> selectByIdCollegeGroup(Long collegeGroupId) {
        if(collegeGroupId == null){
            throw new ServiceException("学院组id不能为null",400);
        }
        List<CollegeGroup> list = this.lambdaQuery()
                .eq(CollegeGroup::getCollegeGroupId, collegeGroupId)
                .list();
        if (list == null || list.size() == 0){
            throw new ServiceException("学院组不存在",400);
        }
        CollegeGroupVo collegeGroupVo = BeanCopyUtils.copyBean(list.get(0), CollegeGroupVo.class);
        return Response.success(collegeGroupVo);
    }

    @Override
    public PageDTO<CollegeDetailedLisVo> getCollegedetailedList(CollegeDto collegeDto) {
        //1.分页模糊查询
        LambdaQueryWrapper<CollegeGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .like(StrUtil.isNotBlank(collegeDto.getName()), CollegeGroup::getName, collegeDto.getName());
        Page<CollegeGroup> page = collegeDto.toMpPageDefaultSortByCreateTimeDesc();
        this.page(page, queryWrapper);
        PageDTO<CollegeDetailedLisVo> pageDTO = PageDTO.of(page, CollegeDetailedLisVo.class);
        return pageDTO;
    }

    @Override
    public List<CollegeDataVo> getCollegeUser(Long id) {
        //1.查询
        List<CollegeData> collegeDataList = collegeDataService.lambdaQuery()
                .eq(CollegeData::getCollegeGroupId, id)
                .list();
        //2.封装返回数据
        if (collegeDataList != null && collegeDataList.size() > 0){
            //2.1返回数据
            List<CollegeDataVo> dataVos = BeanCopyUtils.copyBeans(collegeDataList, CollegeDataVo.class);
            //2.2获取用户的id,查询用户昵称
            List<Long> userIds = dataVos.stream()
                    .map(dataVo -> dataVo.getUserId())
                    .collect(Collectors.toList());
            //2.3 根据用户id查询用户信息
            List<User> users = userService.selectByUserIds(userIds);
            dataVos.stream()
                    .forEach(dataVo -> {
                        users.stream()
                                .filter(user -> user.getUserId().equals(dataVo.getUserId()))
                                .limit(1)
                                .forEach(user -> {
                                    dataVo.setNickName(user.getNickName());
                                });
                    });
            return dataVos;
        }
        return Collections.emptyList();
    }

    @Override
    public boolean updateCollege(UpdateCollegeGroup updateCollegeUserDto) {
        //1.判断修名称是否重复
        if(checkCollegeName(updateCollegeUserDto.getName(),updateCollegeUserDto.getCollegeGroupId())){
            throw new ServiceException("学院组名称重复",400);
        }
        //2.判断是否存在
        if (this.getById(updateCollegeUserDto.getCollegeGroupId()) == null){
            throw new ServiceException("学院组不存在",400);
        }
        //3.修改
        CollegeGroup collegeGroup = BeanCopyUtils.copyBean(updateCollegeUserDto, CollegeGroup.class);
        if(this.updateById(collegeGroup)){
            return true;
        }
        return false;
    }

    @Override
    public boolean addCollegeGroup(AddCollegeGroup addCollegeGroup) {
        //1.先判断学院名称是否重复
        if(checkCollegeName(addCollegeGroup.getName(),null)){
            throw new ServiceException("学院组名称重复",400);
        }
        //2.添加学院组
        CollegeGroup collegeGroup = BeanCopyUtils.copyBean(addCollegeGroup, CollegeGroup.class);
        if(this.save(collegeGroup)){
            return true;
        }
        return false;
    }

    /**
     * 根据学院名称查询,判断是否重复
     *
     */
    private boolean checkCollegeName(String name,Long id) {
        LambdaQueryWrapper<CollegeGroup> queryWrapper = new LambdaQueryWrapper<CollegeGroup>()
                .eq(CollegeGroup::getName, name)
                .notIn(id!=null,CollegeGroup::getCollegeGroupId,id);
        return this.count(queryWrapper) > 0;
    }

}

