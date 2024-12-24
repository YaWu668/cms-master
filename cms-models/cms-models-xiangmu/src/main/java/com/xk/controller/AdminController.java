package com.xk.controller;


import com.cms.common.core.web.domain.Response;
import com.cms.common.log.annotation.Log;
import com.cms.common.log.enums.BusinessType;
import com.xk.entity.SpecialistGroup;
import com.xk.service.SpecialistGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 管理员控制器
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final SpecialistGroupService specialistGroupService;


    @PostMapping("/addannual/addGroup")
    // todo   @RequiresPermissions("xm:admin:insert")
    @Log(title = "添加专家组", businessType = BusinessType.INSERT)
    public Response addSpecialistGroup(@RequestBody SpecialistGroup specialistGroup){
        return specialistGroupService.insertSpecialistGroup(specialistGroup);
    }
}
