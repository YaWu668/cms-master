package com.xk.controller;

import com.cms.common.core.web.domain.Response;
import com.cms.common.security.annotation.RequiresPermissions;
import com.xk.domain.dto.ApplyForDTO;
import com.xk.entity.Project;
import com.xk.service.ProjectService;
import lombok.RequiredArgsConstructor;
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
    public Response applyProject(@RequestBody @Valid ApplyForDTO applyForDTO ) {
        System.out.println("学生申请项目：" + applyForDTO);

        return Response.success();
    }

}
