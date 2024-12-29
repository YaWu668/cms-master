package com.xk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.common.core.web.domain.Response;
import com.xk.domain.dto.AddSpecialistUserDTO;
import com.xk.entity.SpecialistData;

import java.util.List;


/**
 * 专家人员表(SpecialistData)表服务接口
 *
 * @author YaWu
 * @since 2024-12-19 01:42:02
 */
public interface SpecialistDataService extends IService<SpecialistData> {
    /**
     * 获取当前用户的专家组的id
     * @return 专家组id集合
     */
    List<Long> getSpecialistIdByUserId();

    /**
     * 添加专家组人员
     * @param addSpecialistUserDTO
     * @return
     */
    Response addSpecialistUser(AddSpecialistUserDTO addSpecialistUserDTO);
}

