package com.cms.system.controller;

import com.cms.common.core.validation.ValidationGroups;
import com.cms.common.core.web.controller.BaseController;
import com.cms.common.core.web.domain.Response;
import com.cms.common.log.annotation.Log;
import com.cms.common.log.enums.BusinessType;
import com.cms.common.security.annotation.RequiresPermissions;
import com.cms.system.api.domain.pojo.SysDictData;
import com.cms.system.domain.query.SysDictDataQuery;
import com.cms.system.service.SysDictDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统字典数据控制器
 *
 * @author 邓志军
 * @date 2024年6月1日15:33:42
 */
@Api(value = "/dict/data", tags = {"系统字典数据控制器"})
@RestController
@RequestMapping("/dict/data")
public class SysDictDataController extends BaseController {

    @Autowired
    private SysDictDataService dictDataService;

    /**
     * 获取系统字典值数据列表
     *
     * @param dictType 字典类型
     * @param query    查询条件
     * @return 系统字典值数据列表
     */
    @RequiresPermissions("system:dict:query")
    @ApiOperation(value = "获取系统字典值数据列表", notes = "获取系统字典值数据列表", httpMethod = "GET")
    @GetMapping("/list/{dictType}")
    public Response<List<SysDictData>> listEntities(@PathVariable String dictType, SysDictDataQuery query) {
        // 1.开启分页
        // this.startPage();
        // 2.获取列表数据
        List<SysDictData> sysDictdatas = this.dictDataService.listEntities(dictType, query);
        // 3.返回数据
        return this.success(sysDictdatas);
    }

    /**
     * 获取字典数值数据(字典查询)
     *
     * @param dictType 字典类型
     * @return 系统字典值数据
     */
    @ApiOperation(value = "获取字典数值数据(字典查询)", notes = "获取系统字典值数据列表", httpMethod = "GET")
    @GetMapping("/cache/{dictType}")
    public Response<List<SysDictData>> getDictDateList(@PathVariable String dictType) {
        List<SysDictData> sysDictDataList = this.dictDataService.getDictDateList(dictType);
        return this.success(sysDictDataList);
    }

    /**
     * 根据id获取字典数据详情信息
     *
     * @param id 字典数据编码
     * @return 字典数据详情信息
     */
    @RequiresPermissions("system:dict:query")
    @ApiOperation(value = "根据id获取字典数据详情信息", notes = "根据id获取字典数据详情信息", httpMethod = "GET")
    @GetMapping("/{id}")
    public Response<SysDictData> getEntityById(@PathVariable Long id) {
        SysDictData dictData = this.dictDataService.getEntityById(id);
        return this.success(dictData);
    }

    /**
     * 添加系统字典数据
     *
     * @param sysDictData 字典数据详情
     */
    @RequiresPermissions("system:dict:add")
    @ApiOperation(value = "添加系统字典数据", notes = "添加系统字典数据", httpMethod = "POST")
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @PostMapping
    public Response<Boolean> addEntity(@RequestBody @Validated(value = {ValidationGroups.Insert.class}) SysDictData sysDictData) {
        return this.success(this.dictDataService.addEntity(sysDictData));
    }

    /**
     * 修改系统字典数据
     *
     * @param sysDictData 字典数据详情
     */
    @RequiresPermissions("system:dict:edit")
    @ApiOperation(value = "修改系统字典数据", notes = "修改系统字典数据", httpMethod = "PUT")
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public Response<Boolean> updateEntity(@RequestBody @Validated(value = {ValidationGroups.Update.class}) SysDictData sysDictData) {
        return this.success(this.dictDataService.updateEntity(sysDictData));
    }

    /**
     * 根据id删除字典数据
     *
     * @param ids 字典数据字典编码集合
     */
    @RequiresPermissions("system:dict:delete")
    @ApiOperation(value = "根据id删除字典数据", notes = "根据id删除字典数据", httpMethod = "DELETE")
    @Log(title = "字典数据", businessType = BusinessType.DELETE)
    @DeleteMapping("/remove")
    public Response<Boolean> deleteEntityById(@RequestParam("ids") List<Long> ids) {
        boolean flag = this.dictDataService.deleteEntityById(ids);
        return this.success(flag);
    }

}
