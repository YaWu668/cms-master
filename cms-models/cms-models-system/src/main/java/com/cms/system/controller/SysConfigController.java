package com.cms.system.controller;

import com.cms.common.core.validation.ValidationGroups;
import com.cms.common.core.web.controller.BaseController;
import com.cms.common.core.web.domain.Response;
import com.cms.common.core.web.page.TableDataInfo;
import com.cms.common.log.annotation.Log;
import com.cms.common.log.enums.BusinessType;
import com.cms.common.security.annotation.RequiresPermissions;
import com.cms.system.domain.pojo.SysConfig;
import com.cms.system.domain.query.SysConfigQuery;
import com.cms.system.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 系统参数设置
 *
 * @author 邓志军
 * @date 2024年8月14日07:58:08
 */
@Api(value = "/config", tags = {"系统参数设置"})
@RestController
@RequestMapping("/config")
public class SysConfigController extends BaseController {

    @Resource
    private SysConfigService configService;

    /**
     * 查询系统参数设置列表
     *
     * @param configQuery 参数查询条件
     * @return 系统参数设置列表数据
     */
    @RequiresPermissions("system:config:query")
    @ApiOperation(value = "查询系统参数设置列表", notes = "查询系统参数设置列表", httpMethod = "GET")
    @GetMapping("/list")
    public Response<TableDataInfo<SysConfig>> getEntityList(SysConfigQuery configQuery) {
        this.startPage();
        List<SysConfig> configList = this.configService.getEntityList(configQuery);
        return this.getDataTable(configList);
    }

    /**
     * 获取非内置配置数据
     *
     * @return 系统配置
     */
    @ApiOperation(value = "获取非内置配置数据", notes = "获取非内置配置数据", httpMethod = "GET")
    @GetMapping("/sys_config")
    public Response<List<Map<String, String>>> getNoInternallyConfig() {
        return this.success(this.configService.getNoInternallyConfig());
    }

    /**
     * 根据id获取系统配置详情信息
     *
     * @param configId 配置信息id
     * @return 配置详细信息
     */
    @RequiresPermissions("system:config:query")
    @ApiOperation(value = "根据id获取系统配置详情信息", notes = "根据id获取系统配置详情信息", httpMethod = "GET")
    @GetMapping("/{id}")
    public Response<SysConfig> getEntityById(@PathVariable("id") Long configId) {
        return this.success(this.configService.getEntityById(configId));
    }

    /**
     * 修改系统配置详情信息
     *
     * @param config 配置详细信息
     */
    @RequiresPermissions("system:config:edit")
    @ApiOperation(value = "修改系统配置详情信息", notes = "修改系统配置详情信息", httpMethod = "PUT")
    @Log(title = "系统参数", businessType = BusinessType.UPDATE)
    @PutMapping()
    public Response<?> updateEntity(@RequestBody @Validated(value = {ValidationGroups.Update.class}) SysConfig config) {
        return this.success(this.configService.updateEntity(config));
    }

    /**
     * 新增系统配置详情信息
     *
     * @param config 配置详细信息
     */
    @RequiresPermissions("system:config:add")
    @ApiOperation(value = "新增系统配置详情信息", notes = "新增系统配置详情信息", httpMethod = "POST")
    @Log(title = "系统参数", businessType = BusinessType.INSERT)
    @PostMapping
    public Response<?> addEntity(@RequestBody @Validated(value = {ValidationGroups.Insert.class}) SysConfig config) {
        return this.success(this.configService.addEntity(config));
    }

    /**
     * 删除系统配置详情信息
     *
     * @param ids 系统配置id
     */
    @RequiresPermissions("system:config:delete")
    @ApiOperation(value = "删除系统配置详情信息", notes = "删除系统配置详情信息", httpMethod = "DELETE")
    @Log(title = "系统参数", businessType = BusinessType.DELETE)
    @DeleteMapping
    public Response<?> deleteEntity(@RequestParam("ids") List<Long> ids) {
        return this.success(this.configService.deleteEntity(ids));
    }
}
