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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 项目表(Project)表服务接口
 *
 * @author yawu
 */
public interface ProjectService extends IService<Project> {
    /**
     * 根据年度组id导出项目
     * @param yearGroupId 年度组id
     * @param response
     * @return
     */
    boolean downloadProject( Long yearGroupId, HttpServletResponse response);
    /**
     * 批量下载指定项目的文件（支持结题材料或附加材料），并打包成 zip 压缩包通过响应返回。
     *
     * @param projectIds 项目 ID 列表
     * @param fileType   文件类型（1：结题材料；2：附加材料）
     * @param response   HttpServletResponse，用于将 zip 包输出给客户端
     */
    void downloadProjectsFile(List<Long> projectIds, int fileType, HttpServletResponse response);
    /**
     * 提交文件
     * @param submitFileDTO
     * @return
     */
    boolean submitFile( SubmitFileDTO submitFileDTO);

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
    /**
     * 校验成员&指导老师,校验成功返回true,失败返回false
     * @param memberDTO
     * @param msg 错误信息
     * @return true:校验成功,false:校验失败
     */
    boolean calibrationMember( MemberDTO memberDTO, String msg);

    /**
     * 校验立项依据,校验成功返回true,失败返回false
     * @param basisForTheProjectDTO
     * @param msg
     * @return true:校验成功,false:校验失败
     */
    boolean calibrationBasis(BasisForTheProjectDTO basisForTheProjectDTO, String msg);

    /**
     * 校验经费预算,校验成功返回true,失败返回false
     * @param budgetDTO
     * @param msg
     * @return  true:校验成功,false:校验失败
     */
    boolean calibrationBudget(BudgetDTO budgetDTO, String msg);

    /**
     * 根据指导项目id最后添加一个项目进度记录
     * @param projectId
     * @param msg
     */
    void addAProjectProcess(Long projectId, String msg);
    /**
     * 修改项目(只能修改审核为通过的)
     * @param modifyApplyForDTO
     * @return
     */
    Response updateProject( ModifyApplyForDTO modifyApplyForDTO);

    /**
     * 修改项目解题时间
     * @param updateProjectEndTimeDto
     * @return
     */
    Response updateProjectEndTime( UpdateProjectEndTimeDto updateProjectEndTimeDto);

    /**
     * 修改项目解题时间(延期解题)
     * @param updateProjectEndTimeDelayDto
     * @return
     */
    Response updateProjectEndTimeDelay( UpdateProjectEndTimeDelayDto updateProjectEndTimeDelayDto);

    /**
     * 结题操作,可以设置的状态为  3为待结题 4为结题通过 5为结题不通过<br>
     * 一样的状态不可以重复覆盖操作
     * @param updateProjectPassDto
     * @return
     */
    Response updateProjectPass( UpdateProjectPassDto updateProjectPassDto);

    /**
     * 根据项目id集合进行批量分配专家组
     * @param updateProjectSpecialistGroupDto
     * @return
     */
    Response updateProjectSpecialistGroup( UpdateProjectSpecialistGroupDto updateProjectSpecialistGroupDto);

    /**
     * 根据项目id导出对应的word
     * @param projectId 项目id
     * @return 返回文件流，前端下载
     */
     void exportXmWord(Long projectId, HttpServletResponse response) throws Exception;

    /**
     * 读取项目批量绑定项目编号
     * @param file
     * @return 绑定参数集合
     */
    List<BatchBingNumberDto> readBatchBingNumberDto(MultipartFile file);
}

