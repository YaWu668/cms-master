package com.xk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.common.core.web.domain.Response;
import com.xk.domain.dto.AddSpecialistDataDto;
import com.xk.domain.dto.AddSpecialistUserDTO;
import com.xk.domain.dto.PageDTO;
import com.xk.domain.dto.QuerySpecialistData;
import com.xk.domain.vo.specialist.SpecialistDataVo;
import com.xk.entity.SpecialistData;

import javax.validation.Valid;
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

    /**
     * 根据专家组id查询专家人员
     * @param querySpecialistData
     * @return
     */
    PageDTO<SpecialistDataVo> getSpecialistDate(QuerySpecialistData querySpecialistData);

    /**
     * 删除专家人员
     * @param id
     * @return
     */
    boolean deleteSpecialistData(Long id);

    /**
     * 根据用户id删除,专家角色
     * @param userId
     */
    void deleteSpecialistRole(Long userId);

    /**
     * 根据用户id,添加专家角色
     * @param userId
     */
    void addSpecialistRole( Long userId);

    /**
     * 新增人员
     * @param addSpecialistDataDto
     * @return
     */
    boolean addSpecialistData( AddSpecialistDataDto addSpecialistDataDto);

}

