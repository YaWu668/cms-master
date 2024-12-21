package com.xk.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.common.core.web.domain.Response;
import com.xk.domain.dto.ApplyForDTO;
import com.xk.domain.dto.ProjectAuditDto;
import com.xk.entity.Project;

/**
 * 项目表(Project)表服务接口
 *
 * @author yawu
 */
public interface ProjectService extends IService<Project> {
    /**
     * 申请项目
     * @param applyForDTO
     * @return
     */
    Response addProject(ApplyForDTO applyForDTO);

    /**
     * 项目审核,所有角色可以使用该方法进行审核
     * @param projectAuditDto
     * @return
     */
    Response projectAudit(ProjectAuditDto projectAuditDto);

    /**
     * 获取学生参与的项目
     * @return
     */
    Response getStudentProjectList(int currentPage,int pageSize);

    /**
     * 获取学生负责项目
     * @return
     */
    Response getStudentResponsibleProjectList();

    /**
     * 项目负责人删除学生项目,只删除未通过的
     * @param projectId
     * @return
     */
    Response delectStudentProjectById(Long projectId);

    /**
     * 获取学生
     * @param role 角色：学生 0 老师 1
     * @param name 学号/名字/工号
     * @return
     */
    Response getUserApply(int role, String name);

    /**
     * 获取自己创建的的项目
     * @return
     */
    Response getMyCrectProject(int currentPage,int pageSize);
}

