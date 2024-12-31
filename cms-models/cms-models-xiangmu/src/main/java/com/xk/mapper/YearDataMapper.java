package com.xk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xk.entity.Project;
import com.xk.entity.YearData;
import com.xk.entity.YearGroup;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 年度组表(YearData)表数据库访问层
 *
 * @author yawu
 * @since 2024-12-05 22:52:50
 */
public interface YearDataMapper extends BaseMapper<YearData> {
    /**
     * 根据年度组ID和状态查询年度组信息
     *
     * @param yearGroupId 年度组ID
     * @return 年度组列表
     */
    @Select("SELECT * FROM xm_year_group WHERE year_group_id = #{yearGroupId} AND status = 0 LIMIT 1")
    List<YearGroup> findYearGroupByIdAndStatus(@Param("yearGroupId") Long yearGroupId);


    /**
     * 判断是否有项目引用指定年度数据
     *
     * @param id 年度数据ID
     * @return 项目列表
     */
    @Select("SELECT * FROM xm_project WHERE year_data_id = #{id} LIMIT 1")
    List<Project> findProjectByYearDataId(@Param("id") Long id);
}

