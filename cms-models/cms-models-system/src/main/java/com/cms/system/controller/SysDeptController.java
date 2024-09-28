package com.cms.system.controller;

import com.cms.common.core.validation.ValidationGroups;
import com.cms.common.core.web.controller.BaseController;
import com.cms.common.core.web.domain.Response;
import com.cms.common.log.annotation.Log;
import com.cms.common.log.enums.BusinessType;
import com.cms.common.security.annotation.RequiresPermissions;
import com.cms.system.api.domain.dto.SysDeptDto;
import com.cms.system.api.domain.pojo.SysDept;
import com.cms.system.domain.query.SysDeptQuery;
import com.cms.system.domain.vo.TreeSelect;
import com.cms.system.service.SysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统部门控制器
 */
@Api(value = "/dept", tags = {"系统部门控制器"})
@RestController
@RequestMapping("/dept")
public class SysDeptController extends BaseController {

    @Autowired
    private SysDeptService deptService;

    /**
     * 获取部门树列表
     */
    @ApiOperation(value = "获取部门层级树列表", notes = "获取部门树列表", httpMethod = "GET")
    @GetMapping("/deptTree")
    public Response<List<TreeSelect>> getDeptTree(SysDeptDto dept) {
        return this.success(this.deptService.selectDeptTreeList(dept));
    }

    /**
     * 获取部门列表信息
     *
     * @param query 查询条件参数
     * @return 部门列表信息
     */
    @RequiresPermissions("system:menu:remove")
    @ApiOperation(value = "获取部门列表信息", notes = "获取部门列表信息", httpMethod = "GET")
    @GetMapping("/list")
    public Response<List<SysDeptDto>> listEntities(SysDeptQuery query) {
        return this.success(this.deptService.listEntities(query));
    }

    /**
     * 添加部门信息
     *
     * @param dept 部门信息
     */
    @RequiresPermissions("system:menu:add")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public Response<Boolean> addEntity(@RequestBody @Validated(value = {ValidationGroups.Insert.class}) SysDept dept) {
        return this.success(this.deptService.addEntity(dept));
    }

    /**
     * 修改部门信息
     *
     * @param dept 部门信息
     */
    @RequiresPermissions("system:menu:edit")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PutMapping
    public Response<Boolean> updateEntity(@RequestBody @Validated(value = {ValidationGroups.Update.class}) SysDept dept) {
        return this.success(this.deptService.updateEntity(dept));
    }

    /**
     * 查询部门详细信息
     *
     * @param id 部门id
     * @return 部门详情信息
     */
    @RequiresPermissions("system:menu:query")
    @GetMapping("/{id}")
    public Response<SysDept> getEntityById(@PathVariable Long id) {
        return this.success(this.deptService.getEntityById(id));
    }

    /**
     * 根据id删除部门信息
     *
     * @param id 部门id
     */
    @RequiresPermissions("system:menu:delete")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public Response<?> deleteEntityByDeptId(@PathVariable("id") Long id) {
        return this.success(this.deptService.deleteEntityByDeptId(id));
    }
}
