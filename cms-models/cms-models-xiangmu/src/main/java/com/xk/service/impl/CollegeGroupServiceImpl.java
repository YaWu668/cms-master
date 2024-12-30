package com.xk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.core.web.domain.Response;
import com.xk.domain.dto.collegeListDto;
import com.xk.domain.vo.group.CollegeGroupVo;
import com.xk.entity.CollegeGroup;
import com.xk.mapper.CollegeGroupMapper;
import com.xk.service.CollegeGroupService;
import com.xk.utils.BeanCopyUtils;
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
public class CollegeGroupServiceImpl extends ServiceImpl<CollegeGroupMapper, CollegeGroup> implements CollegeGroupService {

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
}

