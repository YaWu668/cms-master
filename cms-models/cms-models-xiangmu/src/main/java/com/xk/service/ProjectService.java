package com.xk.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.common.core.web.domain.Response;
import com.xk.domain.dto.*;

import com.xk.domain.vo.detail.DetailProjectVo;
import com.xk.domain.vo.project.ProjectListvo;

import com.xk.domain.vo.detail.DetailProjectVo;
import com.xk.domain.vo.student.StudentProjectVo;
import com.xk.domain.vo.student.StudentVo;
import com.xk.entity.Project;

import java.util.List;

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
     * 获取学生参与的项目列表
     * @param currentPage 页码
     * @param pageSize 单页大小
     * @return StudentProjectVo
     */
    Response<StudentProjectVo> getStudentProjectList(int currentPage,int pageSize);

    /**
     * 项目负责人删除学生项目,只删除未通过的
     * @param projectId 学生id
     * @return
     */
    Response delectStudentProjectById(Long projectId);

    /**
     * 获取学生
     * @param role 角色：学生 0 老师 1
     * @param name 学号/名字/工号
     * @return
     */
    Response<StudentProjectVo> getUserApply(int role, String name);

    /**
     * 获取自己创建的项目
     * @param currentPage
     * @param pageSize
     * @return
     */
    Response<PageDTO<StudentProjectVo>> getMyCrectProject(int currentPage, int pageSize);

    /**
     *根据项目id查询项目详细信息
     * @param id 项目id
     * @param type 查询类型,1:学生 , 2:教师, 3:学院审核人, 4:专家, 5:管理员
     * @return 项目详细信息
     */
    Response<DetailProjectVo> getProjectById(Long id,String type);



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


    /**
     * 获取名下的某个学生的项目列表
     * @Param studentProjectDto
     * @return 学生列表响应数据
     */
    PageDTO<StudentProjectVo> getStudentProjectList(StudentProjectDto studentProjectDto);

    /**
     *  项目列表,全角色
     * @param projectSelectDto
     * @return 项目列表(分页)
     */
    PageDTO<ProjectListvo> getProjectList(ProjectSelectDto projectSelectDto);

    /**
     * 查询学生(当前用户)项目列表(分页),<br>
     * ps:这个被调用的方法,前缀条件需要自己判断
     * @param projectSelectDto 查询条件
     * @return 项目列表(分页)
     */
    PageDTO<ProjectListvo> getProjectListByStudent(ProjectSelectDto projectSelectDto);

    /**
     * 查询老师的学生项目列表(分页),<br>
     * ps:这个被调用的方法,前缀条件需要自己判断
     * @param projectSelectDto 查询条件
     * @return 项目列表(分页)
     */
    PageDTO<ProjectListvo> getProjectListByTeacher(ProjectSelectDto projectSelectDto);

    /**
     * 查询学院(当前用户)项目列表(分页),<br>
     * ps:这个被调用的方法,前缀条件需要自己判断
     * @param projectSelectDto 查询条件
     * @return 项目列表(分页)
     */
    PageDTO<ProjectListvo> getProjectListByCollege(ProjectSelectDto projectSelectDto);

    /**
     * 查询专家(当前用户)项目列表(分页),<br>
     * ps:这个被调用的方法,前缀条件需要自己判断
     * @param projectSelectDto 查询条件
     * @return 项目列表(分页)
     */
    PageDTO<ProjectListvo> getProjectListBySpecialist(ProjectSelectDto projectSelectDto);

    /**
     * 查询项目(管理员可以查看全部)
     * @param projectSelectDto 查询条件
     * @return 项目列表
     */
    PageDTO<ProjectListvo> getProjectListByAdmin(ProjectSelectDto projectSelectDto);

    /**
     * 教师获取自己绑定的学生的项目列表
     * @param teacherSearchStuentDto
     * @return
     */
    PageDTO<StudentVo> getStudentBindStudent(TeacherSearchStuentDto teacherSearchStuentDto);

    /**
     * 校验基本情况,校验成功返回true,失败返回false
     * @param basicInformationDTO
     * @param msg 错误信息
     * @return true:校验成功,false:校验失败
     */
    boolean calibrationBasicInformation(BasicInformationDTO basicInformationDTO,String msg);
}

