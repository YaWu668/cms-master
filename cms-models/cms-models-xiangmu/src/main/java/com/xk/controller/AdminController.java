package com.xk.controller;


import com.cms.common.core.web.domain.Response;
import com.cms.common.log.annotation.Log;
import com.cms.common.log.enums.BusinessType;
import com.xk.domain.dto.InquireUserDto;
import com.xk.domain.dto.PageDTO;
import com.xk.domain.query.PageQuery;
import com.xk.domain.vo.admin.UserListVo;
import com.xk.entity.SpecialistGroup;
import com.xk.service.SpecialistGroupService;
import com.xk.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


/**
 * 管理员控制器
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final SpecialistGroupService specialistGroupService;

    private final UserService userService;
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
     * 分页查询全部用户信息
     * @param inquireUserDto
     * @return
     */
    @GetMapping("/getAllUser")
    // todo   @RequiresPermissions("xm:admin:select")
    @Log(title = "修改专家组信息", businessType = BusinessType.OTHER)
    public Response<PageDTO<UserListVo>> getUserList(InquireUserDto inquireUserDto){
        return Response.success(userService.getUserList(inquireUserDto));
    }

}
