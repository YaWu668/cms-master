package com.xk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.core.web.domain.Response;
import com.xk.entity.SpecialistGroup;
import com.xk.mapper.SpecialistGroupMapper;
import com.xk.service.SpecialistGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
                if (specialistGroup.getStatus().equals("0")){
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
                .ne(SpecialistGroup::getStatus, "1")
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
