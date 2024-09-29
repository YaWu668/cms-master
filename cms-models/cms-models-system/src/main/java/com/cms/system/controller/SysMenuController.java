package com.cms.system.controller;

import com.cms.common.core.web.controller.BaseController;
import com.cms.common.core.web.domain.Response;
import com.cms.common.log.annotation.Log;
import com.cms.common.log.enums.BusinessType;
import com.cms.common.security.annotation.RequiresPermissions;
import com.cms.common.security.utils.SecurityUtils;
import com.cms.system.domain.dto.SysMenuDto;
import com.cms.system.domain.pojo.SysMenu;
import com.cms.system.domain.query.SysMenuQuery;
import com.cms.system.domain.vo.RouterVo;
import com.cms.system.domain.vo.TreeSelect;
import com.cms.system.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统菜单信息控制器
 *
 * @author 邓志军
 * @date 2024年5月30日16:20:40
 */
@Api(value = "/menu", tags = {"系统菜单信息控制器"})
@RestController
@RequestMapping("/menu")
public class SysMenuController extends BaseController {

    @Autowired
    private SysMenuService menuService;

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @ApiOperation(value = "获取路由信息", notes = "获取路由信息", httpMethod = "GET")
    @GetMapping("getRouters")
    public Response<List<RouterVo>> getRouters() {
        // 1.获取菜单信息
        List<SysMenuDto> menus = this.menuService.selectMenuTreeByUserId(SecurityUtils.getUserId());
        // 2.构建前端需要的菜单
        List<RouterVo> routerVos = this.menuService.buildMenus(menus);
        // 3.返回数据
        return this.success(routerVos);
    }

    /**
     * 查询菜单列表数据
     *
     * @param query 菜单查询条件
     * @return 菜单列表
     */
    @RequiresPermissions("system:menu:query")
    @ApiOperation(value = "查询菜单列表数据", notes = "查询菜单列表数据", httpMethod = "GET")
    @GetMapping("/list")
    public Response<List<SysMenuDto>> listEntities(SysMenuQuery query) {
        // 1.查询所有菜单数据
        List<SysMenuDto> menus = this.menuService.listEntities(query);
        // 2.构建列表树
        return this.success(this.menuService.buildListMenus(menus));
    }

    /**
     * 获取菜单下拉列表树
     */
    @ApiOperation(value = "获取菜单下拉列表树", notes = "获取菜单下拉列表树", httpMethod = "GET")
    @GetMapping("/getMenuTree")
    public Response<List<TreeSelect>> getMenuTree() {
        List<TreeSelect> treeList = this.menuService.getMenuTree();
        return this.success(treeList);
    }

    /**
     * 删除菜单
     *
     * @param id 菜单id
     */
    @RequiresPermissions("system:menu:remove")
    @ApiOperation(value = "删除菜单", notes = "删除菜单", httpMethod = "DELETE")
    @Log(title = "菜单管理",businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public Response<?> updateEntity(@PathVariable Long id) {
        return this.success(this.menuService.deleteEntityById(id));
    }

    /**
     * 查询菜单详情信息
     *
     * @param id 菜单id
     */
    @RequiresPermissions("system:menu:query")
    @ApiOperation(value = "查询菜单详情信息", notes = "查询菜单详情信息", httpMethod = "GET")
    @GetMapping("/{id}")
    public Response<?> getEntity(@PathVariable Long id) {
        return this.success(this.menuService.getEntityById(id));
    }

    /**
     * 添加菜单
     *
     * @param menu 菜单信息
     */
    @RequiresPermissions("system:menu:add")
    @ApiOperation(value = "添加菜单", notes = "添加菜单", httpMethod = "POST")
    @Log(title = "菜单管理",businessType = BusinessType.INSERT)
    @PostMapping
    public Response<?> addEntity(@RequestBody SysMenu menu) {
        return this.success(this.menuService.addEntity(menu));
    }

    /**
     * 修改菜单
     *
     * @param menu 菜单信息
     */
    @RequiresPermissions("system:menu:edit")
    @ApiOperation(value = "修改菜单", notes = "修改菜单", httpMethod = "PUT")
    @Log(title = "菜单管理",businessType = BusinessType.UPDATE)
    @PutMapping
    public Response<?> updateEntity(@RequestBody SysMenu menu) {
        return this.success(this.menuService.updateEntity(menu));
    }
}
