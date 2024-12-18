package com.xk.controller;

import com.cms.common.log.annotation.Log;
import com.cms.common.log.enums.BusinessType;
import com.cms.common.security.annotation.RequiresPermissions;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 公共api
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/public")
public class PulicController {
    @PostMapping(value ="/projectAudit")
    @RequiresPermissions("xm:project:audit")
    @Log(title = "项目审核", businessType = BusinessType.OTHER)
    public String projectAudit() {
        return "projectAudit";
    }
}
