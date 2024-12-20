package com.xk.controller;

import com.cms.common.core.web.domain.Response;
import com.cms.common.log.annotation.Log;
import com.cms.common.log.enums.BusinessType;
import com.cms.common.security.annotation.RequiresPermissions;
import com.xk.domain.dto.ProjectAuditDto;
import com.xk.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * 公共api
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/public")
public class PulicController {
    /**
     * 项目服务
     */
    private final ProjectService projectService;
    /**
     * 项目审核
     * @return
     */
    @PostMapping(value ="/projectAudit")
//    @RequiresPermissions("xm:project:audit")
    @Log(title = "项目审核", businessType = BusinessType.OTHER)
    public Response projectAudit(@RequestBody @Valid ProjectAuditDto projectAuditDto) {
        return projectService.projectAudit(projectAuditDto);
    }
}
