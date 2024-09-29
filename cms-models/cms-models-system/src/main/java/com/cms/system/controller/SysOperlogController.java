package com.cms.system.controller;

import com.cms.common.core.web.controller.BaseController;
import com.cms.common.core.web.domain.Response;
import com.cms.common.core.web.page.TableDataInfo;
import com.cms.common.security.annotation.InnerAuth;
import com.cms.common.security.annotation.RequiresPermissions;
import com.cms.system.api.domain.pojo.SysOperLog;
import com.cms.system.domain.query.SysOperLogQuery;
import com.cms.system.service.SysOperLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统操作日志记录控制器
 *
 * @author 邓志军
 * @dat 2024年5月29日13:54:21
 */
@Api(value = "/operlog", tags = {"系统操作日志记录控制器"})
@RestController
@RequestMapping("/operlog")
public class SysOperlogController extends BaseController {

    @Autowired
    private SysOperLogService operLogService;

    /**
     * 添加日志信息
     *
     * @param operLog 日志信息
     */
    @ApiOperation(value = "添加日志信息", notes = "添加日志信息", httpMethod = "POST")
    @InnerAuth
    @PostMapping
    public Response<?> addEntity(@RequestBody SysOperLog operLog) {
        return this.respond(this.operLogService.addEntity(operLog));
    }

    /**
     * 查询操作日志记录列表分页数据
     *
     * @param query 查询参数
     * @return 操作日志列表
     */
    @RequiresPermissions("system:operlog:query")
    @ApiOperation(value = "查询操作日志记录列表分页数据", notes = "查询操作日志记录列表分页数据", httpMethod = "GET")
    @GetMapping("/list")
    public Response<TableDataInfo<SysOperLog>> listEntities(SysOperLogQuery query) {
        this.startPage();
        List<SysOperLog> logs = this.operLogService.listEntities(query);
        return this.getDataTable(logs);
    }

    /**
     * 根据id获取操作日志详情信息
     *
     * @param id 操作日志id
     * @return 操作日志详情信息
     */
    @RequiresPermissions("system:operlog:query")
    @ApiOperation(value = "根据id获取操作日志详情信息", notes = "根据id获取操作日志详情信息", httpMethod = "GET")
    @GetMapping("/{id}")
    public Response<SysOperLog> getEntityById(@PathVariable("id") Long id) {
        return this.success(this.operLogService.getEntityById(id));
    }
}
