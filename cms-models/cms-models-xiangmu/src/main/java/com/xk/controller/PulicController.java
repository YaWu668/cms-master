package com.xk.controller;

import com.cms.common.core.web.domain.Response;
import com.cms.common.log.annotation.Log;
import com.cms.common.log.enums.BusinessType;
import com.xk.client.SysFileClient;
import com.xk.domain.dto.*;

import com.xk.domain.vo.SearchPersonListVo;
import com.xk.domain.vo.detail.DetailProjectVo;
import com.xk.domain.vo.group.CollegeGroupVo;
import com.xk.domain.vo.group.SpecialistGroupVo;
import com.xk.domain.vo.group.YearGroupVo;
import com.xk.domain.vo.project.ProjectListvo;
import com.xk.domain.dto.ProjectAuditDto;
import com.xk.domain.dto.collegeListDto;

import com.xk.domain.dto.yearListDTO;
import com.xk.domain.vo.detail.DetailProjectVo;
import com.xk.entity.Role;
import com.xk.service.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
     * 专家组服务
     */
    private final SpecialistGroupService specialistGroupService;

    /**
     * 学院服务
     */
    private final CollegeGroupService collegeGroupService;
    /**
     * 角色服务
     */
    private final RoleService roleService;
    /**
     * 用户角色服务
     */
    private final UserRoleService  userRoleService;
    /**
     * 用户服务
     */
    private final UserService userService;

    private final SysFileClient  sysFileClient;
    /**
     * 测试调用文件
     */
    @GetMapping("/file/download")
    public Response<byte[]> downloadFile(@RequestParam("filePath") String filePath) {
        ResponseEntity<byte[]> entity = sysFileClient.viewXmByPath(filePath);
        return Response.success();
    }


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
    public Response<DetailProjectVo> getProjectById(@Valid   ProjectIdDto projectIdDto) {
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

    /**
     * 条件获取(搜索用户)
     * @param projectPersonDto
     * @return
     */
    @GetMapping("/search/person")
    @Log(title = "搜索人员", businessType = BusinessType.OTHER)
    public Response<PageDTO<SearchPersonListVo>> searchPerson(ProjectPersonDto projectPersonDto){
        //获取对应角色集合
        //根据用户id进行条件查询
        PageDTO<SearchPersonListVo> pageDTO = userService.getUsersByRolesAndKeyword(projectPersonDto);
        return Response.success(pageDTO);
    }

    /**
     * 根据id查询学院组
     * @param collegeGroupId 学院组id
     * @return 学院vo类
     */
    @GetMapping("/select/collegeGroup")
    @Log(title = "根据id查询学院组", businessType = BusinessType.OTHER)
    public Response<CollegeGroupVo> selectByIdCollegeGroup(Long collegeGroupId) {
        return collegeGroupService.selectByIdCollegeGroup(collegeGroupId);
    }

    /**
     * 根据id查询专家组
     * @param specialistGroupId
     * @return 专家组vo类
     */
    @GetMapping("/select/specialistGroup")
    @Log(title = "根据id查询专家组", businessType = BusinessType.OTHER)
    public Response<SpecialistGroupVo> selectByIdSpecialistGroup(Long specialistGroupId){
        return specialistGroupService.selectByIdSpecialistGroup(specialistGroupId);
    }
    /**
     * 根据id查询年度组
     * @param yearGroupId 年度组id
     * @return 年度组vo类
     */
    @GetMapping("/select/yearGroup")
    @Log(title = "根据id查询年度组", businessType = BusinessType.OTHER)
    public Response<YearGroupVo> selectByIdYearGroup(Long yearGroupId){
        return yearGroupService.selectByIdYearGroup(yearGroupId);
    }

    /**
     * 根据项目的id，导出对应项目的word书
     * @param projectIds 项目id
     * @return response携带文件字节数组返回
     */
    @GetMapping("/exportWord")
    @Log(title = "批量导出项目word", businessType = BusinessType.EXPORT)
    public void exportXmWord(@RequestParam List<Long> projectIds, HttpServletResponse response) throws Exception {
        // 调用服务层方法获取文件字节数组
        projectService.exportXmWord(projectIds,response);
    }


}
