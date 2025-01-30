package com.xk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xk.entity.SpecialistData;
import com.xk.entity.SpecialistGroup;
import org.apache.ibatis.annotations.Select;


/**
 * 专家人员表(SpecialistData)表数据库访问层
 *
 * @author YaWu
 * @since 2024-12-19 01:42:02
 */
public interface SpecialistDataMapper extends BaseMapper<SpecialistData> {

    /**
     * 根据专家组的id获取到专家组
     */
    @Select("select * from xm_specialist_group where specialist_group_id = #{specialistGroupId}")
    SpecialistGroup getSpecialistGroupBySpecialistGroupId(Long specialistGroupId);
}

