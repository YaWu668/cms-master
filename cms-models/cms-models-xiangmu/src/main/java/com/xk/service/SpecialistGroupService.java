package com.xk.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.common.core.web.domain.Response;
import com.xk.entity.SpecialistGroup;

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
}
