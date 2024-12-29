package com.xk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xk.entity.SpecialistGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SpecialistGroupMapper extends BaseMapper<SpecialistGroup> {

    //查询专家组id是否存在
    @Select("select specialist_group_id from xm_specialist_group where specialist_group_id = #{specialistGroupId}")
    SpecialistGroup selectByIdForSpecialistGroupId(Long specialistGroupId);
}
