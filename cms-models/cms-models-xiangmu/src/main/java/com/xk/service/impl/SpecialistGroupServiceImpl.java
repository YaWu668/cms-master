package com.xk.service.impl;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.core.web.domain.Response;
import com.xk.domain.dto.AddSpecialistGroup;
import com.xk.domain.dto.PageDTO;
import com.xk.domain.dto.QuerySpecialistGroup;
import com.xk.domain.dto.UpdateSpecialistGroup;
import com.xk.domain.vo.group.SpecialistGroupVo;
import com.xk.entity.SpecialistGroup;
import com.xk.mapper.SpecialistGroupMapper;
import com.xk.service.SpecialistGroupService;

import com.xk.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service("SpecialistGroupService")
public class SpecialistGroupServiceImpl extends ServiceImpl<SpecialistGroupMapper, SpecialistGroup> implements SpecialistGroupService {
    @Override
    public Response insertSpecialistGroup(SpecialistGroup specialistGroup) {
        //名字是否存在
        isNameNull(specialistGroup.getName());

        // 调用 save 方法进行保存
        save(specialistGroup);
        return Response.success();
    }

    @Override
    @Transactional
    public Response updateSpecialistGroup(SpecialistGroup specialistGroup) {
        //校验专家组是否存在
        isIdNull(specialistGroup.getSpecialistGroupId());

        //校验专家组是否启用
        switch (isStatusEnable(specialistGroup.getSpecialistGroupId())){
            case 0:
                newUpdateSpecialistGroup(specialistGroup);
                return Response.success();

            case 1:
                //校验修改的信息里是否有启用状态
                if (specialistGroup.getStatus() == 0L){
                    newUpdateSpecialistGroup(specialistGroup);
                    return Response.success();
                }

                throw new ServiceException("专家组未启用",444);

            default:
                throw new ServiceException("字典错误", 444);
        }


    }

    @Override
    public List<SpecialistGroup> selectByIds(List<Long> ids) {
        // 去重
        List<Long> list = ids.stream().distinct().collect(Collectors.toList());
        // 检查列表是否为空
        if (list.isEmpty()) {
            return Collections.emptyList(); // 返回空结果
        }
        return lambdaQuery()
                .in(SpecialistGroup::getSpecialistGroupId, list)
                .list();
    }

    @Override
    public Response<SpecialistGroupVo> selectByIdSpecialistGroup(Long specialistGroupId) {
        if(specialistGroupId == null){
            throw new ServiceException("专家组id不能为null", 400);
        }
        List<SpecialistGroup> list = this.lambdaQuery()
                .eq(SpecialistGroup::getSpecialistGroupId, specialistGroupId)
                .list();
        if (list == null || list.isEmpty()){
            throw new ServiceException("专家组不存在", 400);
        }
        SpecialistGroupVo specialistGroupVo = BeanCopyUtils.copyBean(list.get(0), SpecialistGroupVo.class);
        return Response.success(specialistGroupVo);
    }

    @Override
    public PageDTO<SpecialistGroup> getSpecialistGroupList(QuerySpecialistGroup querySpecialistGroup) {
        Page<SpecialistGroup> page = querySpecialistGroup.toMpPageDefaultSortByCreateTimeDesc();
        LambdaUpdateWrapper<SpecialistGroup> wrapper = new LambdaUpdateWrapper<SpecialistGroup>()
                .like(querySpecialistGroup.getName() != null && StrUtil.isNotBlank(querySpecialistGroup.getName()), SpecialistGroup::getName, querySpecialistGroup.getName());
        this.page(page, wrapper);
        return PageDTO.of(page, SpecialistGroup.class);
    }

    @Override
    @Transactional
    public boolean addSpecialistGroup(AddSpecialistGroup addSpecialistGroup) {
        //1.判断名字是否重复
        if(isRepeat(addSpecialistGroup.getName())){
            throw new ServiceException("专家组名字重复", 444);
        }
        //2.新增
        SpecialistGroup bean = BeanCopyUtils.copyBean(addSpecialistGroup, SpecialistGroup.class);
        if(this.save(bean)){
            return true;
        }
        return false;
    }

    @Override
    public boolean isRepeat(String name) {
        Long count = this.lambdaQuery()
                .eq(SpecialistGroup::getName, name)
                .count();
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean modifySpecialistGroup(UpdateSpecialistGroup updateSpecialistGroup) {
        //1.判断名字是否重复
        if(isRepeat(updateSpecialistGroup.getName())){
            throw new ServiceException("专家组名字重复", 444);
        }
        //2.修改
        SpecialistGroup bean = BeanCopyUtils.copyBean(updateSpecialistGroup, SpecialistGroup.class);
        if(this.updateById(bean)){
            return true;
        }
        return false;
    }

    public void newUpdateSpecialistGroup(SpecialistGroup specialistGroup) {
        //校验要更改的名字有无重复
        newIsNameNull(specialistGroup.getName(),specialistGroup.getSpecialistGroupId());

        // 构建更新条件
        LambdaUpdateWrapper<SpecialistGroup> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SpecialistGroup::getSpecialistGroupId, specialistGroup.getSpecialistGroupId());

        // 调用 update 方法进行更新
        update(specialistGroup, updateWrapper);
    }

    //校验除了自身外要更改的名字是否重复
    private void newIsNameNull(String name,Long id) {
        boolean outcome = lambdaQuery()
                .ne(SpecialistGroup::getSpecialistGroupId, id)
                .eq(SpecialistGroup::getName,name)
                .exists();
        if (outcome){
            throw new ServiceException("专家组名字重复", 444);
        }
    }


    //校验专家组是否启用
    public int isStatusEnable(Long id) {
        return lambdaQuery()
                .eq(SpecialistGroup::getSpecialistGroupId, id)
                .ne(SpecialistGroup::getStatus, 1L)
                .exists()?0:1;
    }

    /**
     * 判断专家组是否存在
     * @param specialistGroupId 专家组id
     */
    public void isIdNull(Long specialistGroupId) {
        boolean outcome =  lambdaQuery()
                .eq(SpecialistGroup::getSpecialistGroupId, specialistGroupId)
                .exists();
        if (!outcome) {
            throw new ServiceException("专家组不存在", 444);
        }
    }

    /**
     * 判断专家组名字是否重复
     * @param name 专家组名字
     */
    public  void isNameNull(String name){
        boolean outcome = lambdaQuery()
                .eq(SpecialistGroup::getName, name)
                .exists();

        if (outcome) {
            throw new ServiceException("专家组名字重复", 444);
        }
    }
}
