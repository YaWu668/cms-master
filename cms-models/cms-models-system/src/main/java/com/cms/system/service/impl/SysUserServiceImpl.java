package com.cms.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.constant.CacheConstants;
import com.cms.common.core.constant.UserConstants;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.core.utils.ExcelUtils;
import com.cms.common.redis.service.RedisService;
import com.cms.common.security.utils.SecurityUtils;
import com.cms.system.api.domain.dto.SysUserDto;
import com.cms.system.api.domain.pojo.SysUser;
import com.cms.system.domain.pojo.SysConfig;
import com.cms.system.domain.query.SysUserQuery;
import com.cms.system.domain.vo.TreeSelect;
import com.cms.system.domain.vo.UserPasswordUpdateVo;
import com.cms.system.domain.vo.UserUpdateStatusVo;
import com.cms.system.mapper.SysPostMapper;
import com.cms.system.mapper.SysRoleMapper;
import com.cms.system.mapper.SysUserMapper;
import com.cms.system.service.SysUserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统用户信息 Service 实现
 *
 * @author 邓志军
 * @date 2024-05-28
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysPostMapper postMapper;

    @Resource
    private SysRoleMapper roleMapper;

    @Resource
    private RedisService redisService;

    /**
     * 查询系统用户信息列表数据(无分页)
     *
     * @param sysUser 系统用户信息
     * @return 系统用户信息列表数据
     */
    @Override
    public List<SysUser> listAllEntities(SysUser sysUser) {
        // 1.构建查询条件对象
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        // 2.返回数据
        return this.sysUserMapper.selectList(queryWrapper);
    }

    /**
     * 查询系统用户信息列表分页数据
     *
     * @param query 系统用户信息
     * @return 系统用户信息列表数据
     */
    @Override
    public List<SysUserDto> listEntities(SysUserQuery query) {
        return this.sysUserMapper.listEntities(query);
    }

    /**
     * 根据id查询系统用户信息详细信息
     *
     * @param id 系统用户信息表数据id
     */
    @Override
    public SysUser getEntityById(Long id) {
        return this.sysUserMapper.getEntityById(id);
    }

    /**
     * 添加系统用户信息数据
     *
     * @param sysUser 系统用户信息
     * @return 添加系统用户信息数据成功返回 true 否则返回 false
     */
    @Override
    public boolean addEntity(SysUserDto sysUser) {
        // 1.添加用户数据
        String encryptPassword = SecurityUtils.encryptPassword(sysUser.getPassword());
        sysUser.setPassword(encryptPassword);
        this.save(sysUser);

        Long userId = sysUser.getUserId();

        // 2.添加岗位绑定关系
        List<Long> postIds = sysUser.getPostIds();
        postIds.forEach(postId -> this.postMapper.buildUserPost(userId, postId));

        // 3.添加所属角色绑定关系
        List<Long> roleIds = sysUser.getRoleIds();
        roleIds.forEach(roleId -> this.roleMapper.authUser(roleId, new Long[]{userId}));

        return true;
    }

    /**
     * 修改系统用户信息数据
     *
     * @param sysUser 系统用户信息
     * @return 修改系统用户信息数据成功返回 true 否则返回 false
     */
    @Override
    public boolean updateEntity(SysUserDto sysUser) {
        Long currentOperationUserId = SecurityUtils.getUserId();
        if (!SecurityUtils.isAdmin(currentOperationUserId)) {
            throw new ServiceException("您没有权限修改超级管理员数据!");
        }

        Long userId = sysUser.getUserId();

        // 1.删除绑定的所有岗位并重新绑定
        this.postMapper.cancelUserBuildPost(sysUser.getUserId());
        List<Long> postIds = sysUser.getPostIds();
        postIds.forEach(postId -> this.postMapper.buildUserPost(userId, postId));

        // 2.删除绑定的所有角色并重新绑定
        this.roleMapper.cancelUserBuildUser(sysUser.getUserId());
        List<Long> roleIds = sysUser.getRoleIds();
        roleIds.forEach(roleId -> this.roleMapper.authUser(roleId, new Long[]{userId}));

        // 3.修改用户信息
        sysUser.setUserName(null);
        return this.updateById(sysUser);
    }

    /**
     * 根据id删除系统用户信息数据
     *
     * @param ids 系统用户信息表id集合
     * @return 删除系统用户信息数据成功返回 true 否则返回 false
     */
    @Override
    public boolean deleteEntityById(List<Long> ids) {
        return this.removeByIds(ids);
    }

    /**
     * 根据用户名称，获取用户信息
     *
     * @param username 用户名称
     * @return 用户信息
     */
    @Override
    public SysUser getUserInfoByUserName(String username) {
        // 1.构建查询条件对象
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUserName, username);
        // 2.查询数据
        return this.sysUserMapper.selectOne(queryWrapper);
    }

    /**
     * 根据用户id查询该用户拥有哪些岗位
     *
     * @param id 用户id
     * @return 岗位id集合
     */
    @Override
    public List<Long> getUserPostIds(Long id) {
        return this.sysUserMapper.getUserPostIds(id);
    }

    /**
     * 根据用户id查询该用户拥有哪些角色
     *
     * @param id 角色id
     * @return 角色id集合
     */
    @Override
    public List<Long> getUserRolesIds(Long id) {
        return this.sysUserMapper.getUserRolesIds(id);
    }

    /**
     * 构建前端用户 select 数据
     */
    @Override
    public List<TreeSelect> buildUserTreeSelect() {
        // 1.查询所有用户
        List<SysUser> sysUsers = this.listAllEntities(new SysUser());
        // 2.构建树数据
        return sysUsers.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 查询已分配用户角色列表
     *
     * @param query 系统用户信息
     * @param id    角色id
     * @return 已分配系统用户信息列表数据
     */
    @Override
    public List<SysUserDto> selectAllocatedList(SysUserQuery query, Long id) {
        return this.sysUserMapper.selectAllocatedList(query, id);
    }

    /**
     * 查询未分配用户角色列表
     *
     * @param query 系统用户信息
     * @param id    角色id
     * @return 未分配系统用户信息列表数据
     */
    @Override
    public List<SysUserDto> selectUnallocatedList(SysUserQuery query, Long id) {
        return this.sysUserMapper.selectUnallocatedList(query, id);
    }

    /**
     * 重置用户密码
     *
     * @param ids 用户id集合
     */
    @Override
    public boolean resetPassword(List<Long> ids) {
        boolean contains = ids.contains(UserConstants.ADMIN_ID);
        if (contains) {
            throw new ServiceException("不允许重置超级管理员密码!!!");
        }
        // 获取配置的默认密码
        SysConfig config = this.redisService.getCacheObject(CacheConstants.DEFAULT_PASSWORD_KEY);

        // 加密默认密码
        String encryptPassword = SecurityUtils.encryptPassword(config.getConfigValue());
        // 重置密码
        ids.forEach(userId -> this.sysUserMapper.updateUserPassword(userId, encryptPassword));

        return true;
    }

    /**
     * 修改用户头像
     *
     * @param userId 用户id
     * @param url    头像地址
     */
    @Override
    public boolean updateAvatar(Long userId, String url) {
        return this.sysUserMapper.updateAvatar(userId, url);
    }

    /**
     * 修改用户信息
     *
     * @param userId         用户id
     * @param passwordUpdate 用户修改密码信息
     */
    @Override
    public boolean updatePassword(Long userId, UserPasswordUpdateVo passwordUpdate) {
        if (!passwordUpdate.confirmPassword()) {
            throw new ServiceException("二次密码不一致!");
        }
        String encryptPassword = SecurityUtils.encryptPassword(passwordUpdate.getPassword());
        return this.sysUserMapper.updateUserPassword(userId, encryptPassword);
    }

    /**
     * 修改用户状态
     */
    @Override
    public boolean updateStatus(UserUpdateStatusVo userUpdateStatus) {
        return this.sysUserMapper.updateStatus(userUpdateStatus);
    }

    /**
     * 导出用户数据
     */
    @Override
    public boolean export(HttpServletResponse response) {
        List<SysUser> list = this.list();
        try {
            ExcelUtils.write(response, "用户信息表", "用户信息", SysUser.class, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 数据导入
     */
    @Override
    public boolean importUserInfo(MultipartFile file) {
        try {
            List<SysUser> read = ExcelUtils.read(file, SysUser.class);
            System.out.println(read);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
