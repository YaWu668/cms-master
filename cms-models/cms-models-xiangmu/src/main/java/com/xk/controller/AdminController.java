package com.xk.controller;


import com.cms.common.core.web.domain.Response;
import com.cms.common.log.annotation.Log;
import com.cms.common.log.enums.BusinessType;
import com.xk.domain.dto.*;
import com.xk.domain.vo.admin.UserListVo;
import com.xk.domain.vo.group.CollegeDataVo;
import com.xk.domain.vo.group.CollegeDetailedLisVo;
import com.xk.domain.vo.group.YearDetailedLisVo;
import com.xk.entity.SpecialistGroup;
import com.xk.entity.YearGroup;
import com.xk.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 管理员控制器
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
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
     * 添加专家组
     * @param specialistGroup
     * @return
     */
    @PostMapping("/addannual/addGroup")
    // todo   @RequiresPermissions("xm:admin:insert")
    @Log(title = "添加专家组", businessType = BusinessType.INSERT)
    public Response addSpecialistGroup(@RequestBody SpecialistGroup specialistGroup){
        return specialistGroupService.insertSpecialistGroup(specialistGroup);
    }


    /**
     * 修改专家组信息
     * @param specialistGroup
     * @return
     */
    @PutMapping("/search/updateGroup")
    // todo   @RequiresPermissions("xm:admin:update")
    @Log(title = "修改专家组信息", businessType = BusinessType.UPDATE)
    public Response SpecialistGroup(@RequestBody SpecialistGroup specialistGroup){
        return specialistGroupService.updateSpecialistGroup(specialistGroup);
    }

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
     * 添加专家组人员
     */
    @PostMapping("/addGroupUser/addSpecialistUser")
    // todo   @RequiresPermissions("xm:admin:select")
    @Log(title = "添加专家组人员", businessType = BusinessType.OTHER)
    public Response addSpecialistUser(@RequestBody AddSpecialistUserDTO addSpecialistUserDTO){
        return specialistDataService.addSpecialistUser(addSpecialistUserDTO);
    }


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
     * @param id 学院id
     * @return 学院人员
     */
    @GetMapping("/getCollegeUser/{id}")
    @Log(title = "根据学院id,查询人员信息", businessType = BusinessType.OTHER)
    public Response<List<CollegeDataVo>> getCollegeUser(@PathVariable("id") Long id){
        return Response.success(collegeGroupService.getCollegeUser(id));
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
     * 根据学院组的id,修改学院组<br>如果修改禁用,学生申请项目就无法绑定
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
     *学院id添加人员
     */
    @PostMapping("/addCollegeUser")
    @Log(title = "学院id添加人员", businessType = BusinessType.INSERT)
    public Response addCollegeUser(@RequestBody @Valid AddCollegeUserDto addCollegeUserDto){
        //根据学院id添加人员
        boolean r = collegeDataService.addCollegeUser(addCollegeUserDto);
        return r ? Response.success() : Response.error("添加人员失败");
    }

    /**
     * 根据学院人员组的id,删除学院人员
     */
    @DeleteMapping("/deleteCollegeUser")
    @Log(title = "根据学院人员组的id,删除学院人员", businessType = BusinessType.DELETE)
    public Response deleteCollegeUser(@RequestParam Long collegeDataId ){
        //根据学院id和用户id,删除学院人员
        boolean r = collegeDataService.deleteCollegeUser(collegeDataId);
        return r ? Response.success() : Response.error("删除人员失败");
    }
}

