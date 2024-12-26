package com.xk.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.common.core.web.domain.Response;
import com.xk.domain.dto.ApplyForDTO;
import com.xk.domain.dto.ProjectAuditDto;

import com.xk.domain.vo.detail.DetailProjectVo;
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

    /**
     *根据项目id查询项目详细信息
     * @param id 项目id
     * @param type 查询类型,1:学生 , 2:教师, 3:学院审核人, 4:专家, 5:管理员
     * @return 项目详细信息
     */
    Response<DetailProjectVo> getProjectById(Long id,Long type);



    /**
     * 判断这个是不是属于当前用户(学生)的项目
     * @param projectId 项目id
     * @return true:是学生创建的项目,false:不是学生创建的项目
     */
    boolean isProjectOwnedByStudent(Long projectId);

    /**
     * 判断项目属不属于当前用户(老师)的学生
     * @param projectId 项目id
     * @return true:是教师的学生创建的项目,false:不是教师的学生创建的项目
     */
    boolean isProjectOfTeacherStudents(Long projectId);

    /**
     * 判断项目属不属于当前用户的学院
     * @param projectId 项目id
     * @return true:是当前用户的学院的项目,false:不是当前用户的学院的项目
     */
    boolean isProjectInSameCollege(Long projectId);

    /**
     * 判断项目属不属于当前用户的专家组
     * @param projectId 项目id
     * @return
     */
    boolean canExpertReviewProject(Long projectId);
}

