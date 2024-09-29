package com.cms.system.controller;

import com.cms.common.core.validation.ValidationGroups;
import com.cms.common.core.web.controller.BaseController;
import com.cms.common.core.web.domain.Response;
import com.cms.common.core.web.page.TableDataInfo;
import com.cms.common.log.annotation.Log;
import com.cms.common.log.enums.BusinessType;
import com.cms.common.security.annotation.RequiresPermissions;
import com.cms.system.domain.pojo.SysPost;
import com.cms.system.domain.query.SysPostListQuery;
import com.cms.system.domain.vo.SysPostListVo;
import com.cms.system.domain.vo.TreeSelect;
import com.cms.system.service.SysPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统岗位控制器
 *
 * @author 邓志军
 * @date 2024年6月1日12:58:44
 */
@Api(value = "/post", tags = {"系统岗位控制器"})
@RestController
@RequestMapping("/post")
public class SysPostController extends BaseController {

    @Autowired
    private SysPostService postService;

    /**
     * 获取岗位树列表
     */
    @ApiOperation(value = "获取岗位树列表", notes = "获取岗位树列表", httpMethod = "GET")
    @GetMapping("/postTree")
    public Response<List<TreeSelect>> getDeptTree() {
        List<TreeSelect> postTree = this.postService.selectDeptTreeList();
        return this.success(postTree);
    }

    /**
     * 获取岗位列表数据
     *
     * @param query 查询参数
     * @return 岗位列表数据
     */
    @RequiresPermissions("system:post:query")
    @ApiOperation(value = "获取岗位列表数据", notes = "获取岗位列表数据", httpMethod = "GET")
    @GetMapping("/list")
    public Response<TableDataInfo<SysPostListVo>> listAllEntities(SysPostListQuery query) {
        this.startPage();
        return this.getDataTable(this.postService.listAllEntities(query));
    }

    /**
     * 添加岗位信息
     *
     * @param post 岗位信息
     */
    @RequiresPermissions("system:post:add")
    @ApiOperation(value = "添加岗位信息", notes = "添加岗位信息", httpMethod = "POST")
    @PostMapping()
    @Log(title = "岗位管理", businessType = BusinessType.INSERT)
    public Response<Boolean> addEntity(@RequestBody @Validated(value = ValidationGroups.Insert.class) SysPost post) {
        return this.success(this.postService.addEntity(post));
    }

    /**
     * 修改岗位信息
     *
     * @param post 岗位信息
     */
    @RequiresPermissions("system:post:edit")
    @ApiOperation(value = "修改岗位信息", notes = "修改岗位信息", httpMethod = "PUT")
    @PutMapping()
    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
    public Response<Boolean> updateEntity(@RequestBody @Validated(value = ValidationGroups.Update.class) SysPost post) {
        return this.success(this.postService.updateEntity(post));
    }

    /**
     * 查询岗位详情信息
     *
     * @param id 岗位id
     * @return 岗位详情信息
     */
    @RequiresPermissions("system:post:query")
    @ApiOperation(value = "查询岗位详情信息", notes = "查询岗位详情信息", httpMethod = "GET")
    @GetMapping("/{id}")
    public Response<SysPost> getEntityById(@PathVariable Long id) {
        return this.success(this.postService.getEntityById(id));
    }

    /**
     * 根据id删岗位信息
     *
     * @param ids 岗位id集合
     */
    @RequiresPermissions("system:post:delete")
    @ApiOperation(value = "根据id删岗位信息", notes = "根据id删岗位信息", httpMethod = "DELETE")
    @Log(title = "岗位管理", businessType = BusinessType.DELETE)
    @DeleteMapping
    public Response<Boolean> deleteEntity(@RequestParam("ids") List<Long> ids) {
        return this.success(this.postService.deleteEntity(ids));
    }
}
