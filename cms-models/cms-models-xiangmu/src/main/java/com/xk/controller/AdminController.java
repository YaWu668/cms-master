package com.xk.controller;


import com.cms.common.core.web.domain.Response;
import com.cms.common.log.annotation.Log;
import com.cms.common.log.enums.BusinessType;
import com.xk.domain.dto.*;
import com.xk.domain.vo.admin.UserListVo;
import com.xk.domain.vo.group.CollegeDataVo;
import com.xk.domain.vo.group.CollegeDetailedLisVo;
import com.xk.domain.vo.group.YearDetailedLisVo;
import com.xk.domain.vo.specialist.SpecialistDataVo;
import com.xk.domain.vo.specialist.SpecialistGroupListVo;
import com.xk.entity.SpecialistData;
import com.xk.entity.SpecialistGroup;
import com.xk.entity.YearGroup;
import com.xk.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 管理员控制器
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Validated
public class AdminController {
    /**
     * 专家组的服务
     */
    private final SpecialistGroupService specialistGroupService;
    /**
     * 用户服务
     */
    private final UserService userService;
    /**
     * 专家人组的服务
     */
    private final SpecialistDataService specialistDataService;

    /**
     * 年度组的服务
     */
    private final YearGroupService yearGroupService;
    /**
     * 年度数据的服务
     */
    private final YearDataService yearDataService;
    /**
     * 学院的服务
     */
    private final CollegeGroupService collegeGroupService;
    /**
     * 学院数据的服务
     */
    private final CollegeDataService collegeDataService;
    /**
     * 项目服务
     */
    private final  ProjectService projectService;
//-------------------------------------------------------

    /**
     * 获取专家组的列表
     * @return
     */
    @GetMapping("/getSpecialistGroup")
    @Log(title = "获取专家组的列表", businessType = BusinessType.OTHER)
    public Response<PageDTO<SpecialistGroupListVo>> getSpecialistGroupList(@Valid QuerySpecialistGroup querySpecialistGroup){
        return Response.success(specialistGroupService.getSpecialistGroupList(querySpecialistGroup));
    }

    /**
     * 新增专家组列表
     */
    @PostMapping("/addSpecialistGroup")
    @Log(title = "添加专家组", businessType = BusinessType.INSERT)
    public Response addSpecialistGroup(@RequestBody @Valid AddSpecialistGroup addSpecialistGroup){
        return specialistGroupService.addSpecialistGroup(addSpecialistGroup)?
                Response.success() : Response.error("添加专家失败");
    }

    /**
     * 更新专家组,禁用状态时候,项目无法绑定
     * @param updateSpecialistGroup
     * @return
     */
    @PutMapping("/modifySpecialistGroup")
    @Log(title = "修改专家组", businessType = BusinessType.UPDATE)
    public Response modifySpecialistGroup(@RequestBody @Valid UpdateSpecialistGroup updateSpecialistGroup){
        return specialistGroupService.modifySpecialistGroup(updateSpecialistGroup)?
                Response.success() : Response.error("修改专家组失败");
    }

    /**
     * 根据专家组的id获取专家人员信息
     * @param querySpecialistData
     * @return
     */
    @GetMapping("/getSpecialistDate")
    @Log(title = "获取专家数据", businessType = BusinessType.OTHER)
    public Response<PageDTO<SpecialistDataVo>> getSpecialistDate(@Valid QuerySpecialistData querySpecialistData){
        return Response.success(specialistDataService.getSpecialistDate(querySpecialistData));
    }

    /**
     * 新增专家人员(会添加专家角色)
     * @param addSpecialistDataDto
     * @return
     */
    @PostMapping("/addSpecialistData")
    @Log(title = "新增专家人员", businessType = BusinessType.INSERT)
    public Response addSpecialistData(@RequestBody @Valid AddSpecialistDataDto addSpecialistDataDto){
        return specialistDataService.addSpecialistData(addSpecialistDataDto)?
                Response.success() : Response.error("新增专家人员失败");
    }
    /**
     * 根据专家人员的id删除专家人员信息(会删除专家角色)
     */
    @DeleteMapping("/deleteSpecialistData")
    @Log(title = "删除专家人员数据", businessType = BusinessType.DELETE)
    public Response deleteSpecialistData(@RequestParam Long id){
        return specialistDataService.deleteSpecialistData(id)?
                Response.success() : Response.error("删除专家人员失败");
    }

    /**
     * 添加专家组(废弃)
     * @param specialistGroup
     * @return
     */
    /*@PostMapping("/addannual/addGroup")
    // todo   @RequiresPermissions("xm:admin:insert")
    @Log(title = "添加专家组", businessType = BusinessType.INSERT)
    public Response addSpecialistGroup(@RequestBody SpecialistGroup specialistGroup){
        return specialistGroupService.insertSpecialistGroup(specialistGroup);
    }*/


    /**
     * 修改专家组信息(废弃)
     * @param specialistGroup
     * @return
     */
    /*@PutMapping("/search/updateGroup")
    // todo   @RequiresPermissions("xm:admin:update")
    @Log(title = "修改专家组信息", businessType = BusinessType.UPDATE)
    public Response SpecialistGroup(@RequestBody SpecialistGroup specialistGroup){
        return specialistGroupService.updateSpecialistGroup(specialistGroup);
    }*/

    /**
     * (废案)
     * 分页查询全部用户信息
     * @param inquireUserDto
     * @return
     */
    /*@GetMapping("/getAllUser")
    // todo   @RequiresPermissions("xm:admin:select")
    @Log(title = "分页查询全部用户信息", businessType = BusinessType.OTHER)
    public Response<PageDTO<UserListVo>> getUserList(InquireUserDTO inquireUserDto){
        return Response.success(userService.getUserList(inquireUserDto));
    }*/

    /**
     * 添加专家组人员(废弃)
     */
    /*@PostMapping("/addGroupUser/addSpecialistUser")
    // todo   @RequiresPermissions("xm:admin:select")
    @Log(title = "添加专家组人员", businessType = BusinessType.OTHER)
    public Response addSpecialistUser(@RequestBody AddSpecialistUserDTO addSpecialistUserDTO){
        return specialistDataService.addSpecialistUser(addSpecialistUserDTO);
    }*/


//-------------------------------------------------------
    /**
     * 查询年度详细列表
     * @param yearDetailedListDto 查询条件
     * @return 全部年度组的详细数据
     */
    @GetMapping("/getYearDetailedList")
    @Log(title = "查询年度详细列表", businessType = BusinessType.OTHER)
    public Response<PageDTO<YearDetailedLisVo>> getYearDetailedList(YearDetailedListDto yearDetailedListDto){
        return Response.success(yearGroupService.getYearDetailedList(yearDetailedListDto));
    }
    /**
     * 添加年度组
     * @param addYearGroup 年度组
     * @return
     */
    @PostMapping("/yearGroup")
    @Log(title = "添加年度组", businessType = BusinessType.INSERT)
    public Response addYearGroup(@RequestBody @Valid AddYearGroup addYearGroup){
        return yearGroupService.addYearGroup(addYearGroup);
    }

    /**
     * 根据年度组Id进行修改后年度组,年度组禁用的话,<br>学生申请项目无法绑定禁用的
     */
    @PutMapping("/yearGroup")
    @Log(title = "修改年度组", businessType = BusinessType.UPDATE)
    public Response updateYearGroup(@RequestBody @Valid UpdateYearGroup updateYearGroup){
        return yearGroupService.updateYearGroup(updateYearGroup);
    }

    /**
     * 根据年度组id,给年度数据,新增数据
     */
    @PostMapping("/yearData")
    @Log(title = "新增年度数据", businessType = BusinessType.INSERT)
    public Response addYearData(@RequestBody @Valid AddYearDataDto addYearDataDto){
        return yearDataService.addYearData(addYearDataDto);
    }

    /**
     * 根据年度数据的id,删除年度数据
     * @param id 年度数的id
     * @return
     */
    @DeleteMapping("/yearData/{id}")
    @Log(title = "删除年度数据", businessType = BusinessType.DELETE)
    public Response deleteYearData(@PathVariable("id") Long id){
        return yearDataService.deleteYearData(id);
    }

    //--------------

    /**
     * 获取学院详细信息列表
     * @param collegeDto 学院名模糊查询
     * @return
     */
    @GetMapping("/getCollegedetailedList")
    @Log(title = "获取学院详细信息列表", businessType = BusinessType.OTHER)
    public Response<PageDTO<CollegeDetailedLisVo>> getCollegedetailedList(CollegeDto collegeDto){
        return Response.success(collegeGroupService.getCollegedetailedList(collegeDto));
    }

    /**
     * 根据学院id,查询人员信息
     * @param dto 学院id
     * @return 学院人员
     */
    @GetMapping("/getCollegeUser")
    @Log(title = "根据学院id,查询人员信息", businessType = BusinessType.OTHER)
    public Response<PageDTO<CollegeDataVo>> getCollegeUser(  selectByIdCollegeDateDto dto){
        return Response.success(collegeGroupService.getCollegeUser(dto));
    }

    /**
     * 新增学院组
     * @param addCollegeGroup
     * @return
     */
    @PostMapping("/addCollegeGroup")
    @Log(title = "新增学院组", businessType = BusinessType.INSERT)
    public Response addCollegeGroup(@RequestBody @Valid AddCollegeGroup addCollegeGroup){
        //新增学院组
        boolean r = collegeGroupService.addCollegeGroup(addCollegeGroup);
        return r ? Response.success() : Response.error("新增学院组失败");
    }

    /**
     * 根据学院组的id,修改学院组<br>如果修改禁用,学生申请项目就无法绑定,同时禁止增删入员
     * @param updateCollegeUserDto
     * @return
     */
    @PutMapping("/updateCollegeUser")
    @Log(title = "根据学院组的id,修改学院组", businessType = BusinessType.UPDATE)
    public Response updateCollegeUser(@RequestBody @Valid UpdateCollegeGroup updateCollegeUserDto){
        //根据学院id修改学院组
        boolean r = collegeGroupService.updateCollege(updateCollegeUserDto);
        return r ? Response.success() : Response.error("修改学院组失败");
    }
    /**
     *学院id添加人员(会添加学院角色)
     */
    @PostMapping("/addCollegeUser")
    @Log(title = "学院id添加人员", businessType = BusinessType.INSERT)
    public Response addCollegeUser(@RequestBody @Valid AddCollegeUserDto addCollegeUserDto){
        //根据学院id添加人员
        boolean r = collegeDataService.addCollegeUser(addCollegeUserDto);
        return r ? Response.success() : Response.error("添加人员失败");
    }

    /**
     * 根据学院人员组的id,删除学院人员(会删除学院角色)
     */
    @DeleteMapping("/deleteCollegeUser")
    @Log(title = "根据学院人员组的id,删除学院人员", businessType = BusinessType.DELETE)
    public Response deleteCollegeUser(@RequestParam Long collegeDataId ){
        //根据学院id和用户id,删除学院人员
        boolean r = collegeDataService.deleteCollegeUser(collegeDataId);
        return r ? Response.success() : Response.error("删除人员失败");
    }

    //-----------------结题操作-----
    /**
     * 修改解题时间
     */
    @PutMapping("/updateProjectEndTime")
    @Log(title = "修改解题时间", businessType = BusinessType.UPDATE)
    public Response updateProjectEndTime(@RequestBody @Valid UpdateProjectEndTimeDto updateProjectEndTimeDto){
        return projectService.updateProjectEndTime(updateProjectEndTimeDto);
    }

    /**
     * 延期解题
     */
    @PutMapping("/updateProjectEndTimeDelay")
    @Log(title = "延期解题", businessType = BusinessType.UPDATE)
    public Response updateProjectEndTimeDelay(@RequestBody @Valid UpdateProjectEndTimeDelayDto updateProjectEndTimeDelayDto){
        return projectService.updateProjectEndTimeDelay(updateProjectEndTimeDelayDto);
    }

    /**
     * 项目进行解题操作(解题通过或者是撤回解题)
     */
    @PutMapping("/updateProjectPass")
    @Log(title = "项目进行解题操作", businessType = BusinessType.UPDATE)
    public Response updateProjectPass(@RequestBody @Valid UpdateProjectPassDto updateProjectPassDto){
        return projectService.updateProjectPass(updateProjectPassDto);
    }
//----------根据项目id集合进行批量分配专家组-----------

    /**
     * 根据项目id集合进行批量分配专家组
     * @param updateProjectSpecialistGroupDto
     * @return
     */
    @PutMapping("/updateProjectSpecialistGroup")
    @Log(title = "根据项目id集合进行批量分配专家组", businessType = BusinessType.UPDATE)
    public Response updateProjectSpecialistGroup(@RequestBody @Valid UpdateProjectSpecialistGroupDto updateProjectSpecialistGroupDto){
        return projectService.updateProjectSpecialistGroup(updateProjectSpecialistGroupDto);
    }

    //----

    /**
     * 批量根据用户信息进行绑定角色
     */
    @PostMapping("/importUserBindingRole")
    @Log(title = "批量导入用户信息,并且同时绑定指定角色", businessType = BusinessType.INSERT)
    public Response<?> importUserBindingRole(@RequestPart("file") MultipartFile file,
                               @RequestPart("rolekey") @NotBlank(message = "角色key不允许为空") String rolekey){
        //批量导入用户信息,并且同时绑定指定角色
        boolean r = userService.importUserBindingRole(file, rolekey);
        return r ? Response.success("导入成功") : Response.error("导入失败");
    }

    /**
     * 批量根据用户信息进行解绑角色
     */
    @DeleteMapping("/importUserUnbindingRole")
    @Log(title = "批量导入用户信息,并且同时解绑指定角色", businessType = BusinessType.DELETE)
    public Response<?> importUserUnbindingRole(@RequestPart("file") MultipartFile file,
                                               @RequestPart("rolekey") @NotBlank(message = "角色key不允许为空") String rolekey){
        //批量导入用户信息,并且同时解绑指定角色
        boolean r = userService.importUserUnbindingRole(file, rolekey);
        return r ? Response.success("解绑成功") : Response.error("解绑失败");
    }

    /**
     * 批量导入用户信息
     */
    @PostMapping("/importUser")
    @Log(title = "批量导入用户信息", businessType = BusinessType.INSERT)
    public Response<?> importUser(@RequestPart("file") MultipartFile file){
        //批量导入用户信息
        boolean r = userService.importUser(file);
        return r ? Response.success("导入成功") : Response.error("导入失败");
    }

    /**
     * 获取Execl文件模板,0获取绑定和解绑模板,1获取导入用户模板
     */
    @GetMapping("/getExeclTemplate")
    public Response<?> getExeclTemplate(@RequestParam("type")
                                            @NotNull(message = "type不允许为空")
                                            @Min(value = 0,message = "type不允许小于0")
                                            @Max(value = 1,message = "type不允许大于1")
                                            Integer type,
                                        HttpServletResponse response){
        //获取Execl文件模板,0获取绑定和解绑模板,1获取导入用户模板
        return userService.getExeclTemplate(type,response) ?Response.success():Response.error("获取失败");
    }

}

