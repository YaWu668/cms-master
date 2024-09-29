package com.cms.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cms.common.core.web.controller.BaseController;
import com.cms.common.core.web.page.PageParams;
import com.cms.common.core.web.page.TableDataInfo;
import com.cms.common.core.web.domain.Response;
import com.cms.common.security.annotation.InnerAuth;
import com.cms.common.security.annotation.RequiresPermissions;
import com.cms.system.api.domain.pojo.SysLogininfor;
import com.cms.system.domain.query.SysLogininforQuery;
import com.cms.system.service.SysLogininforService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 系统登录记录控制器
 *
 * @author 邓志军
 * @date 2024年5月29日14:24:32
 */
@Api(value = "/logininfor", tags = {"系统登录记录控制器"})
@RestController
@RequestMapping("/logininfor")
public class SysLogininforController extends BaseController {

    @Autowired
    private SysLogininforService sysLogininforService;

    /**
     * 添加登录日志信息
     *
     * @param logininfor 登录日志信息
     */
    @ApiOperation(value = "添加登录日志信息", notes = "添加登录日志信息", httpMethod = "POST")
    @InnerAuth
    @PostMapping
    public Response<?> add(@RequestBody SysLogininfor logininfor) {
        return this.respond(sysLogininforService.addEntity(logininfor));
    }

    /**
     * 查询登录日志列表
     *
     * @param query      登录信息查询参数
     * @param pageParams 分页参数
     * @return 登录日志列表数据
     */
    @RequiresPermissions("system:logininfor:query")
    @ApiOperation(value = "查新登录日志列表", notes = "查新登录日志列表", httpMethod = "GET")
    @GetMapping("/list")
    public Response<TableDataInfo<SysLogininfor>> listEntities(SysLogininforQuery query, PageParams pageParams) {
        IPage<SysLogininfor> page = this.sysLogininforService.listEntities(query, pageParams);
        return this.transitionPageList(page);
    }
}
