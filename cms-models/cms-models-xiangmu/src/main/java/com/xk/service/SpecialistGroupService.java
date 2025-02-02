package com.xk.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.common.core.web.domain.Response;
import com.xk.domain.dto.*;
import com.xk.domain.vo.group.SpecialistGroupVo;
import com.xk.domain.vo.specialist.SpecialistGroupListVo;
import com.xk.entity.SpecialistGroup;

import javax.validation.Valid;
import java.util.List;

/**
 * 专家组表服务接口
 */
public interface SpecialistGroupService extends IService<SpecialistGroup> {
    /**
     * 添加专家组
     * @param specialistGroup
     * @return
     */
    Response insertSpecialistGroup(SpecialistGroup specialistGroup);

    /**
     * 修改专家组信息
     * @param specialistGroup
     * @return
     */
    Response updateSpecialistGroup(SpecialistGroup specialistGroup);

    /**
     * 根据id查询专家组信息
     * @param ids 专家组id
     * @return 专家组列表
     */
    List<SpecialistGroup> selectByIds(List<Long> ids);

    /**
     * 根据id查询专家组信息
     * @param specialistGroupId 专家组id
     * @return vo类
     */
    Response<SpecialistGroupVo> selectByIdSpecialistGroup(Long specialistGroupId);

    /**
     * 查询专家组列表(高级查询)
     * @param querySpecialistGroup
     * @return
     */
    PageDTO<SpecialistGroupListVo> getSpecialistGroupList(QuerySpecialistGroup querySpecialistGroup);

    /**
     * 添加专家组
     * @param addSpecialistGroup
     * @return
     */
    boolean addSpecialistGroup( AddSpecialistGroup addSpecialistGroup);

    /**
     * 判断名字重复
     * @param name 专家组的名称
     * @return true:重复 false:不重复
     */
    boolean isRepeat(String name,Long id);

    /**
     * 修改专家组
     * @param updateSpecialistGroup
     * @return
     */
    boolean modifySpecialistGroup( UpdateSpecialistGroup updateSpecialistGroup);
}
