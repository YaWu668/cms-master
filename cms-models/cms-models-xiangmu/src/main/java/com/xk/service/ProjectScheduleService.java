package com.xk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xk.entity.ProjectSchedule;

import java.util.List;


/**
 * 项目进度记录表(ProjectSchedule)表服务接口
 *
 * @author makejava
 * @since 2024-12-07 01:10:18
 */
public interface ProjectScheduleService extends IService<ProjectSchedule> {

    /**
     * 根据项目id获取项目进度最后一条记录的实体类<br>
     * @param projectId 项目id
     * @return 项目进度最后一条记录的实体类
     */
    ProjectSchedule getLastByProjectId(Long projectId);

    /**
     * 根据项目id获取项目进度列表<br>
     * @param projectId 项目id
     * @throws Exception  项目id不存在异常
     * @return 项目进度列表
     */
    List<ProjectSchedule> listByProjectId(Long projectId);
}

