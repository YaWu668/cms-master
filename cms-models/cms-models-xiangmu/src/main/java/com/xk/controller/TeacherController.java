package com.xk.controller;

import com.cms.common.core.web.domain.Response;
import com.cms.common.log.annotation.Log;
import com.cms.common.log.enums.BusinessType;
import com.xk.domain.dto.PageDTO;
import com.xk.domain.dto.StudentProjectDto;
import com.xk.domain.dto.TeacherSearchStuentDto;
import com.xk.domain.vo.student.StudentProjectVo;
import com.xk.domain.vo.student.StudentVo;
import com.xk.service.ProjectService;
import com.xk.service.StudnetApplysService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 教师
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/teacher")
public class TeacherController {

    /**
     * 服务对象
     */
    private final StudnetApplysService studnetApplysService;
    private final ProjectService projectService;

    /**
     * 批量下载指定项目的文件（支持结题材料或附加材料），并打包成 zip 压缩包通过响应返回。
     *
     * @param projectIds 项目 ID 列表
     * @param fileType   文件类型（1：结题材料；2：附加材料）
     * @param response   HttpServletResponse，用于将 zip 包输出给客户端
     */
    @GetMapping("/download")
    public void downloadFiles(@RequestParam("projectIds") @NotNull List<Long> projectIds,
                                          @RequestParam("fileType") @NotNull Integer fileType,
                                          HttpServletResponse response){

        projectService.downloadProjectsFile(projectIds, fileType, response);
    }

    /**
     * 获取教师绑定的所有学生列表
     * @param currentPage 页码
     * @param pageSize 单页大小
     * @param StudentId 学生id
     * @param StudentName 学生姓名
     * @param StudentSchool 学生学院
     * @return 学生分页列表
     */
    @GetMapping("/searchStudentList")
    // todo 权限 暂时不管
    @Log(title = "获取绑定项目的学生",businessType = BusinessType.OTHER)
    // todo 分页默认值常量
    public Response<PageDTO<StudentVo>> getStudentBindStudent(TeacherSearchStuentDto teacherSearchStuentDto) {
        return Response.success(projectService.getStudentBindStudent(teacherSearchStuentDto));
    }

    /**
     * 获取根据学生id,去获取绑定当前用户的项目
     * @param studentProjectDto 查询条件
     * @return 项目列表
     */
    @GetMapping("/studeng/projectList")
    @Log(title = "获取绑定的学生的所有项目")
    public Response<PageDTO<StudentProjectVo>> getStudentProjectList(StudentProjectDto studentProjectDto){
        return Response.success(projectService.getStudentProjectList(studentProjectDto));
    }



}
