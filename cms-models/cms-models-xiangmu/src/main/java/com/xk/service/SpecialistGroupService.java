package com.xk.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.common.core.web.domain.Response;
import com.xk.domain.dto.AddSpecialistUserDTO;
import com.xk.entity.SpecialistGroup;

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


}
