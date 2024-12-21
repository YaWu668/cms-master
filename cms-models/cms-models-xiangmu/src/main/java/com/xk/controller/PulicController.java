package com.xk.controller;

import com.cms.common.core.web.domain.Response;
import com.cms.common.log.annotation.Log;
import com.cms.common.log.enums.BusinessType;
import com.cms.common.security.annotation.RequiresPermissions;
import com.xk.domain.dto.ProjectAuditDto;
import com.xk.domain.dto.yearListDTO;
import com.xk.service.ProjectService;
import com.xk.service.YearGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


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
     * 年度组服务
     */
    private final YearGroupService yearGroupService;
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
}
