package com.xk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xk.entity.CollegeData;
import com.xk.entity.CollegeGroup;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * 学院组人员表(CollegeData)表数据库访问层
 *
 * @author YaWu
 * @since 2024-12-19 00:50:30
 */
public interface CollegeDataMapper extends BaseMapper<CollegeData> {

    /**
     * 根据学院组id查询是否学院组是否启用了
     * @param collegeGroupId 学院组id
     * @return 学院组是否启用了
     */
    @Select("SELECT DISTINCT count(college_group_id) FROM xm_college_group WHERE `status` = #{status} AND college_group_id = #{collegeGroupId}")
    int isCollegeGroupStatus(@Param("collegeGroupId") Long collegeGroupId,@Param("status")Long status);

    /**
     * 根据学院组id查询学院组信息
     * @param collegeGroupId 学院组id
     * @return
     */
    @Select("select * from xm_college_group where college_group_id =#{collegeGroupId}")
    CollegeGroup getCollegeGroupById(@Param("collegeGroupId") Long collegeGroupId);
}

