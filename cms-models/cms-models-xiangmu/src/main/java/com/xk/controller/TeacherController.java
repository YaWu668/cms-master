package com.xk.controller;

import com.cms.common.core.web.domain.Response;
import com.cms.common.log.annotation.Log;
import com.cms.common.log.enums.BusinessType;
import com.xk.service.ProjectService;
import com.xk.service.StudnetApplysService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

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
    public Response getStudentBindStudent(
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
            @RequestParam(value = "StudentId", defaultValue = "") String StudentId,
            @RequestParam(value = "StudentName",defaultValue = "") String StudentName,
            @RequestParam(value = "StudentSchool",defaultValue = "") String StudentSchool

    ) {
        return studnetApplysService.getBindStudent(currentPage,pageSize,StudentId,StudentName,StudentSchool);
    }

    /**
     * 获取绑定的学生的所有项目
     * @param currentPage 页码
     * @param pageSize 单页大小
     * @param StudentId 学生id
     * @return 项目列表
     */
    @GetMapping("/{studentid}/projectList")
    @Log(title = "获取绑定的学生的所有项目")
    public Response getStudentProjectList(
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
            @PathVariable(value = "studentid") Long StudentId
    ){
        return projectService.getStudentProjectList(currentPage,pageSize,StudentId);
    }


}
