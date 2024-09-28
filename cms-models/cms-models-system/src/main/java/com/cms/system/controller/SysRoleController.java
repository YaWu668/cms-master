package com.cms.system.controller;

import com.cms.common.core.web.controller.BaseController;
import com.cms.common.core.web.domain.Response;
import com.cms.common.core.web.page.TableDataInfo;
import com.cms.common.log.annotation.Log;
import com.cms.common.log.enums.BusinessType;
import com.cms.common.security.annotation.RequiresPermissions;
import com.cms.system.api.domain.dto.SysUserDto;
import com.cms.system.api.domain.pojo.SysRole;
import com.cms.system.domain.vo.SysRoleListVo;
import com.cms.system.domain.pojo.SysMenu;
import com.cms.system.domain.query.SysRoleQuery;
import com.cms.system.domain.query.SysUserQuery;
import com.cms.system.domain.vo.RoleAddVo;
import com.cms.system.domain.vo.RoleDateScope;
import com.cms.system.domain.vo.TreeSelect;
import com.cms.system.service.SysRoleService;
import com.cms.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统角色控制器
 *
 * @author 邓志军
 * @date 2024年6月3日16:27:06
 */
@Api(value = "/role", tags = {"系统角色控制器"})
@RestController
@RequestMapping("/role")
public class SysRoleController extends BaseController {

    @Resource
    private SysRoleService roleService;

    @Resource
    private SysUserService userService;

    /**
     * 查询角色信息列表
     *
     * @param query 角色信息查询条件
     * @return 角色列表
     */
    @RequiresPermissions("system:role:query")
    @ApiOperation(value = "查询角色信息列表", notes = "查询角色信息列表", httpMethod = "GET")
    @GetMapping("/list")
    public Response<TableDataInfo<SysRoleListVo>> listEntities(SysRoleQuery query) {
        this.startPage();
        List<SysRoleListVo> sysRoleListVos = this.roleService.listEntities(query);
        return this.getDataTable(sysRoleListVos);
    }

    /**
     * 查询角色详情信息
     *
     * @param id 角色id
     * @return 角色详情信息
     */
    @ApiOperation(value = "查询角色详情信息", notes = "查询角色详情信息", httpMethod = "GET")
    @RequiresPermissions("system:role:query")
    @GetMapping("/{id}")
    public Response<SysRoleListVo> getEntityById(@PathVariable("id") Long id) {
        SysRoleListVo roleListVo = this.roleService.getEntityById(id);
        return this.success(roleListVo);
    }

    /**
     * 新增角色信息
     *
     * @param role 角色信息
     */
    @ApiOperation(value = "新增角色信息", notes = "新增角色信息", httpMethod = "POST")
    @RequiresPermissions("system:role:add")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping
    public Response<Boolean> addEntity(@RequestBody @Validated RoleAddVo role) {
        boolean result = this.roleService.addEntity(role);
        return this.success(result);
    }

    /**
     * 修改角色信息
     *
     * @param role 角色信息
     */
    @RequiresPermissions("system:role:edit")
    @ApiOperation(value = "修改角色信息", notes = "修改角色信息", httpMethod = "PUT")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public Response<Boolean> updateEntity(@RequestBody @Validated SysRole role) {
        boolean result = this.roleService.updateEntity(role);
        return this.success(result);
    }

    /**
     * 根据id删除角色信息
     *
     * @param ids 角色id集合
     */
    @ApiOperation(value = "根据id删除角色信息", notes = "根据id删除角色信息", httpMethod = "DELETE")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping
    public Response<Boolean> deleteEntityById(@RequestParam("ids") List<Long> ids) {
        boolean result = this.roleService.deleteEntityById(ids);
        return this.success(result);
    }

    /**
     * 获取角色菜单权限
     *
     * @param id 角色id
     * @return 角色拥有的菜单权限
     */
    @ApiOperation(value = "获取角色菜单权限", notes = "获取角色菜单权限", httpMethod = "GET")
    @GetMapping("/menuPermission/{id}")
    public Response<List<SysMenu>> getRoleMenuPermission(@PathVariable Long id) {
        List<SysMenu> roleMenuPermission = this.roleService.getRoleMenuPermission(id);
        return this.success(roleMenuPermission);
    }

    /**
     * 修改角色菜单权限
     *
     * @param ids 菜单id集合
     * @param id  角色id
     */
    @RequiresPermissions("system:role:edit")
    @ApiOperation(value = "修改角色菜单权限", notes = "修改角色菜单权限", httpMethod = "PUT")
    @Log(title = "角色管理 / 修改角色菜单", businessType = BusinessType.UPDATE)
    @PutMapping("/roleMenu/{id}")
    public Response<Boolean> updateRoleMenu(@RequestBody List<Long> ids, @PathVariable("id") Long id) {
        boolean result = this.roleService.updateRoleMenu(ids, id);
        return this.success(result);
    }

    /**
     * 获取前端岗位选择
     *
     * @return 岗位选择树
     */
    @ApiOperation(value = "获取前端岗位选择", notes = "获取前端岗位选择", httpMethod = "GET")
    @GetMapping("/roleTree")
    public Response<List<TreeSelect>> getRoleTree() {
        List<TreeSelect> roleTree = this.roleService.getRoleTree();
        return this.success(roleTree);
    }

    /**
     * 查询已分配用户角色列表
     *
     * @param query 系统用户信息
     * @param id    角色id
     * @return 已分配系统用户信息列表数据
     */
    @ApiOperation(value = "查询已分配用户角色列表", notes = "查询已分配用户角色列表", httpMethod = "GET")
    @RequiresPermissions("system:role:list")
    @GetMapping("/authUser/allocatedList/{id}")
    public Response<TableDataInfo<SysUserDto>> allocatedList(SysUserQuery query, @PathVariable("id") Long id) {
        startPage();
        List<SysUserDto> list = this.userService.selectAllocatedList(query, id);
        return this.getDataTable(list);
    }

    /**
     * 查询未分配用户角色列表
     *
     * @param query 系统用户信息
     * @param id    角色id
     * @return 未分配系统用户信息列表数据
     */
    @ApiOperation(value = "查询未分配用户角色列表", notes = "查询未分配用户角色列表", httpMethod = "GET")
    @RequiresPermissions("system:role:list")
    @GetMapping("/authUser/unallocatedList/{id}")
    public Response<TableDataInfo<SysUserDto>> unallocatedList(SysUserQuery query, @PathVariable("id") Long id) {
        startPage();
        List<SysUserDto> list = this.userService.selectUnallocatedList(query, id);
        return this.getDataTable(list);
    }

    /**
     * 取消角色用户授权
     *
     * @param roleId  角色id
     * @param userIds 用户id集合
     */
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理 / 取消用户授权", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "取消角色用户授权", notes = "取消角色用户授权", httpMethod = "PUT")
    @PutMapping("/authUser/cancel")
    public Response<Boolean> cancelAuthUser(Long roleId, Long[] userIds) {
        return this.success(this.roleService.cancelAuthUser(roleId, userIds));
    }

    /**
     * 角色用户授权
     *
     * @param roleId  角色id
     * @param userIds 用户id集合
     */
    @RequiresPermissions("system:role:edit")
    @ApiOperation(value = "添加角色用户授权", notes = "角色用户授权", httpMethod = "PUT")
    @PutMapping("/authUser/accredit")
    @Log(title = "角色管理 / 添加用户授权", businessType = BusinessType.UPDATE)
    public Response<Boolean> addAuthUser(Long roleId, Long[] userIds) {
        return this.success(this.roleService.authUser(roleId, userIds));
    }

    /**
     * 修改角色状态
     *
     * @param roleId 角色id
     * @param status 角色状态
     */
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理 / 角色状态", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "修改角色状态", notes = "修改角色状态", httpMethod = "PUT")
    @PutMapping("/authUser/status")
    public Response<Boolean> updateRoleStatus(Long roleId, Integer status) {
        return this.success(this.roleService.updateRoleStatus(roleId, status));
    }

    /**
     * 获取角色数据权限信息
     *
     * @param id 角色id
     * @return 数据权限信息
     */
    @RequiresPermissions("system:role:edit")
    @ApiOperation(value = "获取角色数据权限信息", notes = "获取角色数据权限信息", httpMethod = "GET")
    @GetMapping("/authUser/dateScope/{id}")
    public Response<RoleDateScope> getRolDateScope(@PathVariable Long id) {
        RoleDateScope dateScope = this.roleService.getRolDateScope(id);
        return this.success(dateScope);
    }

    /**
     * 修改角色数据权限信息
     *
     * @param dateScope 修改信息
     */
    @RequiresPermissions("system:role:edit")
    @ApiOperation(value = "修改角色数据权限信息", notes = "修改角色数据权限信息", httpMethod = "GET")
    @Log(title = "角色管理 / 数据权限", businessType = BusinessType.UPDATE)
    @PutMapping("/authUser/dateScope")
    public Response<Boolean> updateRolDateScope(@RequestBody @Validated RoleDateScope dateScope) {
        boolean result = this.roleService.updateRolDateScope(dateScope);
        return this.success(result);
    }
}
