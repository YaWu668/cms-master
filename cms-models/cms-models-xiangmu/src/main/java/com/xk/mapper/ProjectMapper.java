package com.xk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xk.entity.Project;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 项目表(Project)表数据库访问层
 *
 * @author yawu
 */
public interface ProjectMapper extends BaseMapper<Project> {

    /**
     * 获取可以结题的项目列表,要求项目状态为项目进行中,并且项目结束时间<当前日期
     */
    @Select("SELECT * FROM xm_project WHERE state = 2 AND end_time < CURDATE()")
    List<Project> getToBeCompletedProjectList();

}

