package com.cms.system.controller;

import com.cms.common.core.utils.StringUtils;
import com.cms.common.core.validation.ValidationGroups;
import com.cms.common.core.web.controller.BaseController;
import com.cms.common.core.web.domain.Response;
import com.cms.common.core.web.page.TableDataInfo;
import com.cms.common.log.annotation.Log;
import com.cms.common.log.enums.BusinessType;
import com.cms.common.security.annotation.RequiresPermissions;
import com.cms.system.api.domain.pojo.SysDictType;
import com.cms.system.domain.query.SysDictTypeQuery;
import com.cms.system.service.SysDictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统字典类型控制器
 *
 * @author 邓志军
 * @date 2024年6月1日15:33:42
 */
@Api(value = "/dict/type", tags = {"系统字典类型控制器"})
@RestController
@RequestMapping("/dict/type")
public class SysDictTypeController extends BaseController {

    @Autowired
    private SysDictTypeService dictTypeService;

    /**
     * 获取系统字典类型数据列表
     *
     * @param query 查询条件
     * @return 系统字典类型数据列表数据
     */
    @RequiresPermissions("system:dict:query")
    @ApiOperation(value = "获取系统字典类型数据列表", notes = "获取系统字典类型数据列表", httpMethod = "GET")
    @GetMapping("/list")
    public Response<TableDataInfo<SysDictType>> listEntities(SysDictTypeQuery query) {
        // 1.开启分页
        this.startPage();
        // 2.转换日期对象
        if (!StringUtils.isNull(query.getBeginTime(), query.getEndTime())) {
            this.modifyObjectDateFields(query);
        }
        // 3.获取列表数据
        List<SysDictType> sysDictTypes = this.dictTypeService.listEntities(query);
        // 4.返回数据
        return this.getDataTable(sysDictTypes);
    }

    /**
     * 根据id获取字典类型详情信息
     *
     * @param id 字典id
     * @return 字典类型详情信息
     */
    @RequiresPermissions("system:dict:query")
    @ApiOperation(value = "根据id获取字典类型详情信息", notes = "根据id获取字典类型详情信息", httpMethod = "GET")
    @GetMapping("/{id}")
    public Response<SysDictType> getEntityById(@PathVariable Long id) {
        SysDictType dictType = this.dictTypeService.getEntityById(id);
        return this.success(dictType);
    }

    /**
     * 添加字典类型数据
     *
     * @param dictType 字典类型数据
     */
    @RequiresPermissions("system:dict:add")
    @ApiOperation(value = "添加字典类型数据", notes = "添加字典类型数据", httpMethod = "POST")
    @Log(title = "字典类型",businessType = BusinessType.INSERT)
    @PostMapping
    public Response<Boolean> addEntity(@RequestBody @Validated(value = {ValidationGroups.Insert.class}) SysDictType dictType) {
        boolean row = this.dictTypeService.addEntity(dictType);
        return this.success(row);
    }

    /**
     * 根据id删除字典类型数据
     *
     * @param ids 字典类型id集合
     */
    @RequiresPermissions("system:dict:delete")
    @ApiOperation(value = "根据id删除字典类型数据", notes = "根据id删除字典类型数据", httpMethod = "DELETE")
    @Log(title = "字典类型",businessType = BusinessType.DELETE)
    @DeleteMapping("/remove")
    public Response<Boolean> deleteEntityById(@RequestParam("ids") List<Long> ids) {
        boolean flag = this.dictTypeService.deleteEntityById(ids);
        return this.success(flag);
    }

    /**
     * 修改字典类型信息
     *
     * @param dictType 字典类型信息
     */
    @RequiresPermissions("system:dict:edit")
    @ApiOperation(value = "修改字典类型信息", notes = "修改字典类型信息", httpMethod = "PUT")
    @Log(title = "字典类型",businessType = BusinessType.UPDATE)
    @PutMapping
    public Response<Integer> updateEntity(@Validated(value = {ValidationGroups.Update.class}) @RequestBody SysDictType dictType) {
        int row = this.dictTypeService.updateEntity(dictType);
        return this.success(row);
    }
}
