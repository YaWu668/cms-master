package com.cms.system.controller;

import com.alibaba.fastjson.JSON;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.core.utils.SpringUtils;
import com.cms.common.core.utils.StringUtils;
import com.cms.common.core.validation.ValidationGroups;
import com.cms.common.core.web.controller.BaseController;
import com.cms.common.core.web.domain.Response;
import com.cms.common.core.web.page.TableDataInfo;
import com.cms.common.log.annotation.Log;
import com.cms.common.log.enums.BusinessType;
import com.cms.common.security.annotation.InnerAuth;
import com.cms.common.security.annotation.RequiresPermissions;
import com.cms.common.security.utils.SecurityUtils;
import com.cms.system.api.domain.dto.SysDeptDto;
import com.cms.system.api.domain.dto.SysUserDto;
import com.cms.system.api.domain.pojo.SysUser;
import com.cms.system.domain.query.SysUserQuery;
import com.cms.system.domain.vo.*;
import com.cms.system.service.SysDeptService;
import com.cms.system.service.SysPermissionService;
import com.cms.system.service.SysUserService;
import com.cms.system.api.model.LoginUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

/**
 * 系统用户信息控制器
 *
 * @author 邓志军
 * @date 2024年5月28日18:13:56
 */
@Api(value = "/user", tags = {"系统用户信息控制器"})
@EnableAutoConfiguration
@RestController
@RequestMapping("/user")
public class SysUserController extends BaseController {

    @Resource
    private SysUserService userService;

    @Resource
    private SysPermissionService permissionService;

    @Resource
    private SysDeptService deptService;

    /**
     * 根据用户名称，获取用户信息
     *
     * @param username 用户名称
     * @return 用户信息
     */
    @ApiOperation(value = "根据用户名称，获取用户信息", notes = "根据用户名称，获取用户信息", httpMethod = "GET")
    @InnerAuth
    @GetMapping("/info/{username}")
    public Response<LoginUser> getUserInfoByUserName(@PathVariable("username") String username) {
        // 1.获取用户信息
        SysUser sysUser = this.userService.getUserInfoByUserName(username);
        if (StringUtils.isNull(sysUser)) {
            return Response.error("用户名或密码错误");
        }
        SysUserDto sysUserDto = SpringUtils.copyProperties(sysUser, SysUserDto.class);
        // 2.获取角色集合
        Set<String> roles = permissionService.getRolePermission(sysUserDto);
        // 3.获取权限集合
        Set<String> permissions = permissionService.getMenuPermission(sysUserDto);
        // 4.封装登录信息
        LoginUser loginUser = new LoginUser();
        loginUser.setSysUser(sysUserDto);
        loginUser.setRoles(roles);
        loginUser.setPermissions(permissions);
        // 5.返回数据
        return Response.success(loginUser);
    }

    /**
     * 根据用户ID，获取用户信息
     *
     * @param id 用户id
     * @return 用户信息
     */
    @ApiOperation(value = "根据用户ID，获取用户信息", notes = "根据用户ID，获取用户信息", httpMethod = "GET")
    @GetMapping("/userInfo/{id}")
    @RequiresPermissions("system:user:query")
    public Response<UserInfoVo> getEntityById(@PathVariable("id") Long id) {
        // 1.查询用户信息
        SysUser user = this.userService.getEntityById(id);
        user.setPassword(null);
        SysUserDto sysUserDto = SpringUtils.copyProperties(user, SysUserDto.class);
        // 2.查询用户有哪些岗位
        List<Long> roleIds = this.userService.getUserRolesIds(id);
        // 3.查询用户有哪些角色
        List<Long> postIds = this.userService.getUserPostIds(id);
        // 4.返回数据
        return this.success(new UserInfoVo(sysUserDto, roleIds, postIds));
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @ApiOperation(value = "获取用户信息", notes = "获取用户信息", httpMethod = "GET")
    @GetMapping("/getInfo")
    public Response<UserInfoVo> getUserInfo() {
        // 1.获取当前登录的用户信息
        SysUser sysUser = this.userService.getEntityById(SecurityUtils.getUserId());
        SysUserDto sysUserDto = SpringUtils.copyProperties(sysUser, SysUserDto.class);
        // 2.获取角色信息
        Set<String> roles = this.permissionService.getRolePermission(sysUserDto);
        // 3.获取权限集合
        Set<String> permissions = permissionService.getMenuPermission(sysUserDto);
        // 4.返回数据
        return this.success(new UserInfoVo(sysUserDto, roles, permissions));
    }

    /**
     * 查询用户列表数据
     *
     * @param query 登录信息查询参数
     */
    @RequiresPermissions("system:user:query")
    @ApiOperation(value = "查询用户列表数据", notes = "查询用户列表数据", httpMethod = "GET")
    @GetMapping("/list")
    public Response<TableDataInfo<SysUserDto>> listEntities(SysUserQuery query) {
        // 1.设置请求分页数据
        this.startPage();
        // 2.查询数据
        List<SysUserDto> users = this.userService.listEntities(query);
        this.userService.setRole(users);
        // 3.返回数据
        return this.getDataTable(users);
    }

    /**
     * 获取部门树列表
     */
    @ApiOperation(value = "获取部门树列表", notes = "获取部门树列表", httpMethod = "GET")
    @GetMapping("/deptTree")
    public Response<List<TreeSelect>> getDeptTree(SysDeptDto dept) {
        return this.success(this.deptService.selectDeptTreeList(dept));
    }

    /**
     * 构建前端用户 select 数据
     */
    @ApiOperation(value = "构建前端用户 select 数据", notes = "构建前端用户 select 数据", httpMethod = "GET")
    @GetMapping("/userTree")
    public Response<List<TreeSelect>> buildUserTreeSelect() {
        return this.success(this.userService.buildUserTreeSelect());
    }

    /**
     * 添加用户信息
     *
     * @param sysUser 用户信息
     */
    @RequiresPermissions("system:user:add")
    @ApiOperation(value = "添加用户信息", notes = "添加用户信息", httpMethod = "POST")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public Response<?> addEntity(@RequestBody SysUserDto sysUser) {
        return this.success(this.userService.addEntity(sysUser));
    }

    /**
     * 重置用户密码
     *
     * @param ids 用户集合
     */
    @RequiresPermissions("system:user:resetPwd")
    @ApiOperation(value = "重置用户密码", notes = "重置用户密码", httpMethod = "PUT")
    @Log(title = "用户管理/重置密码", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPassword")
    public Response<?> resetPassword(@RequestParam("ids") List<Long> ids) {
        return this.success(this.userService.resetPassword(ids));
    }

    /**
     * 修改用户信息
     *
     * @param sysUser 用户信息
     */
    @RequiresPermissions("system:user:edit")
    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", httpMethod = "PUT")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public Response<?> updateEntity(@RequestBody @Validated(value = {ValidationGroups.Update.class}) SysUserDto sysUser) {
        return this.success(this.userService.updateEntity(sysUser));
    }

    /**
     * 修改用户头像
     *
     * @param url 头像地址
     */
    @ApiOperation(value = "修改用户头像", notes = "修改用户头像", httpMethod = "PUT")
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PutMapping("/avatar")
    public Response<?> updateAvatar(@RequestBody String url) {
        String newUrl = (String) JSON.parseObject(url).get("url");
        if (StringUtils.isEmpty(newUrl)) {
            throw new ServiceException("缺少必要参数 url");
        }
        // 获取用户id
        Long userId = SecurityUtils.getUserId();
        return this.success(this.userService.updateAvatar(userId, newUrl));
    }

    /**
     * 修改用户信息
     *
     * @param userInfo 用户信息
     */
    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", httpMethod = "PUT")
    @Log(title = "用户信息", businessType = BusinessType.UPDATE)
    @PutMapping("/update/info")
    public Response<?> updateUserInfo(@RequestBody @Validated UserInfoUpdateVo userInfo) {
        SysUser sysUser = SpringUtils.copyProperties(userInfo, SysUser.class);
        if (StringUtils.isNull(sysUser)) {
            return this.error("用户登录信息获取失败!");
        }
        sysUser.setUserId(SecurityUtils.getUserId());
        return this.success(this.userService.updateById(sysUser));
    }

    /**
     * 修改用户密码
     *
     * @param passwordUpdate 用户修改密码信息
     */
    @ApiOperation(value = "修改密码", notes = "修改密码", httpMethod = "PUT")
    @Log(title = "修改密码", businessType = BusinessType.UPDATE)
    @PutMapping("/update/password")
    public Response<?> updatePassword(@RequestBody @Validated UserPasswordUpdateVo passwordUpdate) {
        return this.success(this.userService.updatePassword(SecurityUtils.getUserId(), passwordUpdate));
    }

    /**
     * 修改用户状态
     */
    @RequiresPermissions("system:user:edit")
    @ApiOperation(value = "修改用户状态", notes = "修改用户状态", httpMethod = "PUT")
    @Log(title = "用户状态", businessType = BusinessType.UPDATE)
    @PutMapping("/update/status")
    public Response<?> updateStatus(@RequestBody @Validated UserUpdateStatusVo userUpdateStatus) {
        return this.success(this.userService.updateStatus(userUpdateStatus));
    }

    /**
     * 数据导出
     */
    @RequiresPermissions("system:user:export")
    @ApiOperation(value = "数据导出", notes = "数据导出", httpMethod = "GET")
    @Log(title = "用户信息",businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        this.userService.export(response);
    }

    /**
     * 数据导入
     */
    @RequiresPermissions("system:user:import")
    @ApiOperation(value = "数据导入", notes = "数据导入", httpMethod = "GET")
    @Log(title = "用户信息",businessType = BusinessType.IMPORT)
    @PostMapping("/import")
    public Response<?> importUserInfo(@RequestBody MultipartFile file) {
        this.userService.importUserInfo(file);
        return this.success();
    }
}
