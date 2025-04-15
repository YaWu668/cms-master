package com.xk.controller;

import com.cms.common.core.web.domain.Response;
import com.cms.common.log.annotation.Log;
import com.cms.common.log.enums.BusinessType;
import com.xk.domain.dto.*;
import com.xk.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * 学生控制器
 * @author yawu
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController  {
    /**
     * 服务对象
     */
    private final ProjectService projectService;

    /**
     * 提交解题文件
     */
    @PostMapping("/submit/file")
    @Log(title = "提交解题文件", businessType = BusinessType.INSERT)
    public Response submitFile(@RequestBody @Valid SubmitFileDTO submitFileDTO) {
        return projectService.submitFile(submitFileDTO)?Response.success():Response.error("提交失败");
    }


    /**
     * 申请项目
     */
    @PostMapping("/applyFor")
//todo    @RequiresPermissions("xm:student:applyFor")
    @Log(title = "项目申请", businessType = BusinessType.INSERT)
    public Response applyProject(@RequestBody @Valid ApplyForDTO  applyForDTO) {
        return projectService.addProject(applyForDTO);
    }

    /**
     * 修改未通过审核的项目
     * todo 修改时候有没有修改我设置那个指针,不然会出问题
     * @param modifyApplyForDTO
     * @return
     */
    @PutMapping("/update")
    @Log(title = "修改未通过审核的项目", businessType = BusinessType.UPDATE)
    public Response updateProject(@RequestBody @Valid ModifyApplyForDTO modifyApplyForDTO) {
        return projectService.updateProject(modifyApplyForDTO);
    }

    /**
     * 获取自己创建的项目列表或者是直接报名项目
     * @return
     */
    @GetMapping("/get/project")
    // todo 权限 暂时不管
    @Log(title = "已参与项目或报名项目",businessType = BusinessType.OTHER)
    // todo 分页默认值常量
    public Response getStudentProjectList(
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(value = "pageSize",defaultValue = "10") int pageSize
    ) {
        return projectService.getStudentProjectList(currentPage,pageSize);
    }


    /**
     * 根据ID删除未通过的项目
     * @param projectId
     * @return
     */
    @DeleteMapping("/delete/{projectId}")
    // todo 权限 暂时不管
    // todo 分页默认值常量
    @Log(title = "删除没有审核通过的项目",businessType = BusinessType.DELETE)
    // 只删除没有审核通过的，并且当前角色是该项目负责人的
    public Response delectProjectById(@PathVariable("projectId") @Valid Long projectId) {
        return projectService.delectStudentProjectById(projectId);
    }

    /**
     * 获取学生 or 教师的报名表
     * @param role
     * @param name
     * @return
     */
    @GetMapping("/getUserList/{role}/{name}")
    // todo 权限
    @Log(title = "用户名获取用户信息",businessType = BusinessType.OTHER)
    //老师或者学生
    public Response getUserApplyListByName(@PathVariable("role") int role, @PathVariable("name") String name) {
        return projectService.getUserApply(role,name);
    }

    /**
     * 搜索自己创建的项目
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping("/search/project")
    // todo 权限 暂时不管
    // todo 分页默认值常量
    @Log(title = "搜索自己创建的项目",businessType = BusinessType.OTHER)
    public Response searchProject(
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(value = "pageSize",defaultValue = "10") int pageSize
    ) {
        return projectService.getMyCrectProject(currentPage,pageSize);
    }

    /**
     * 校验项目基本情况
     */
    @PostMapping("/calibration/basicInformation")
    @Log(title = "校验项目基本情况",businessType = BusinessType.OTHER)
    public Response calibrationBasicInformation(@RequestBody @Valid BasicInformationDTO basicInformationDTO) {
        String msg = "";
        return projectService.calibrationBasicInformation(basicInformationDTO,msg)?Response.success():Response.error(msg);
    }

    /**
     * 校验项目成员&指导老师
     */
    @PostMapping("/calibration/member")
    @Log(title = "校验项目成员&指导老师",businessType = BusinessType.OTHER)
    public Response calibrationMember(@RequestBody @Valid MemberDTO memberDTO) {
        String msg = "";
        return projectService.calibrationMember(memberDTO,msg)?Response.success():Response.error(msg);
    }

    /**
     * 立项依据校验
     */
    @PostMapping("/calibration/basis")
    @Log(title = "校验立项依据",businessType = BusinessType.OTHER)
    public Response calibrationBasis(@RequestBody @Validated BasisForTheProjectDTO basisForTheProjectDTO) {
        String msg = "";
        return projectService.calibrationBasis(basisForTheProjectDTO,msg)?Response.success():Response.error(msg);
    }
    /**
     * 经费预算
     */
    @PostMapping("/calibration/budget")
    @Log(title = "校验经费预算",businessType = BusinessType.OTHER)
    public Response calibrationBudget(@RequestBody @Validated BudgetDTO budgetDTO) {
        String msg = "";
        return projectService.calibrationBudget(budgetDTO,msg)?Response.success():Response.error(msg);
    }

}
