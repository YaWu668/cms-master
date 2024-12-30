package com.xk.controller;

import com.cms.common.core.web.domain.Response;
import com.cms.common.log.annotation.Log;
import com.cms.common.log.enums.BusinessType;
import com.xk.domain.dto.*;

import com.xk.domain.vo.detail.DetailProjectVo;
import com.xk.domain.vo.project.ProjectListvo;
import com.xk.domain.dto.ProjectAuditDto;
import com.xk.domain.dto.collegeListDto;

import com.xk.domain.dto.yearListDTO;
import com.xk.domain.vo.detail.DetailProjectVo;
import com.xk.service.CollegeGroupService;
import com.xk.service.ProjectService;
import com.xk.service.YearGroupService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;


/**
 * 公共api
 */
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/public")
public class PulicController {
    /**
     * 项目服务
     */
    private final ProjectService projectService;
    /**
     * 年度组服务
     */
    private final YearGroupService yearGroupService;

    /**
     * 学院服务
     */
    private final CollegeGroupService collegeGroupService;
    /**
     * 项目审核
     * @return
     */
    @PostMapping(value ="/projectAudit")
//    @RequiresPermissions("xm:project:audit")//特殊处了学生,其他所有人都可以审核
    @Log(title = "项目审核", businessType = BusinessType.OTHER)
    public Response projectAudit(@RequestBody @Valid ProjectAuditDto projectAuditDto) {
        return projectService.projectAudit(projectAuditDto);
    }

    /**
     * 获取年度组和年度数据列表的
     */
    @GetMapping(value ="/yearList")
//    @RequiresPermissions("xm:year:list")//全部人都有
    @Log(title = "获取年度组和年度数据列表的", businessType = BusinessType.OTHER)
    public Response<List<yearListDTO>> yearList(String name) {
        return yearGroupService.yearList(name);
    }

    /**
     * 获取学院列表
     * @param name 学院名称(模糊查询)
     * @return 学院列表
     */
    @GetMapping(value ="/collegeList")
    @Log(title = "获取学院列表", businessType = BusinessType.OTHER)
    public Response<List<collegeListDto>> collegeList(String name) {
        return collegeGroupService.collegeList(name);
    }

    /**
     *根据项目id查询项目详细信息
     * @param id 项目id
     * @param type 查询类型,1:学生 , 2:教师, 3:学院审核人, 4:专家, 5:管理员
     * @return 项目详细信息
     */
    @Log(title = "根据项目id查询项目详细信息", businessType = BusinessType.OTHER)
    @GetMapping("/detail")
    public Response<DetailProjectVo> getProjectById(@Valid @ModelAttribute   ProjectIdDto projectIdDto) {
        return projectService.getProjectById(projectIdDto.getId(), projectIdDto.getType());
    }


    /**
     * 获取项目列表,全角色通用
     * @param projectSelectDto
     * @return
     */
    @GetMapping("/list")
    @Log(title = "获取项目列表(全角色)", businessType = BusinessType.OTHER)
    public Response<PageDTO<ProjectListvo>> getProjectList(ProjectSelectDto projectSelectDto) {
        return Response.success(projectService.getProjectList(projectSelectDto));
    }


}
