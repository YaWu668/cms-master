package com.xk.controller;

import com.cms.common.core.web.domain.Response;
import com.cms.common.log.annotation.Log;
import com.cms.common.log.enums.BusinessType;
import com.xk.domain.dto.ApplyForA;
import com.xk.domain.dto.ApplyForB;
import com.xk.domain.dto.ApplyForC;
import com.xk.domain.dto.ApplyForDTO;
import com.xk.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
     * 申请项目
     */
    @PostMapping("/applyFor")
//todo    @RequiresPermissions("xm:student:applyFor")
    @Log(title = "项目申请", businessType = BusinessType.INSERT)
    public Response applyProject(@RequestBody @Valid ApplyForDTO  applyForDTO) {
        return projectService.addProject(applyForDTO);
    }

}
