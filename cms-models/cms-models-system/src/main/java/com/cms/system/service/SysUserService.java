package com.cms.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.system.api.domain.dto.SysUserDto;
import com.cms.system.api.domain.pojo.SysUser;
import com.cms.system.domain.query.SysUserQuery;
import com.cms.system.domain.vo.TreeSelect;
import com.cms.system.domain.vo.UserPasswordUpdateVo;
import com.cms.system.domain.vo.UserUpdateStatusVo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 系统用户信息 Service 接口
 *
 * @author 邓志军
 * @date 2024-05-28
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 查询系统用户信息列表数据(无分页)
     *
     * @param sysUser 系统用户信息
     * @return 系统用户信息列表数据
     */
    List<SysUser> listAllEntities(SysUser sysUser);

    /**
     * 查询系统用户信息列表分页数据
     *
     * @param query 系统用户信息
     * @return 系统用户信息列表数据
     */
    List<SysUserDto> listEntities(SysUserQuery query);

    /**
     * 根据id查询系统用户信息详细信息
     *
     * @param id 系统用户信息表数据id
     */
    SysUser getEntityById(Long id);

    /**
     * 添加系统用户信息数据
     *
     * @param sysUser 系统用户信息
     * @return 添加系统用户信息数据成功返回 true 否则返回 false
     */
    @Transactional
    boolean addEntity(SysUserDto sysUser);

    /**
     * 修改系统用户信息数据
     *
     * @param sysUser 系统用户信息
     * @return 修改系统用户信息数据成功返回 true 否则返回 false
     */
    boolean updateEntity(SysUserDto sysUser);

    /**
     * 根据id删除系统用户信息数据
     *
     * @param ids 系统用户信息表id集合
     * @return 删除系统用户信息数据成功返回 true 否则返回 false
     */
    boolean deleteEntityById(List<Long> ids);

    /**
     * 根据用户名称，获取用户信息
     *
     * @param username 用户名称
     * @return 用户信息
     */
    SysUser getUserInfoByUserName(String username);

    /**
     * 根据用户id查询该用户拥有哪些岗位
     *
     * @param id 用户id
     * @return 岗位id集合
     */
    List<Long> getUserPostIds(Long id);

    /**
     * 根据用户id查询该用户拥有哪些角色
     *
     * @param id 角色id
     * @return 角色id集合
     */
    List<Long> getUserRolesIds(Long id);

    /**
     * 构建前端用户 select 数据
     */
    List<TreeSelect> buildUserTreeSelect();

    /**
     * 查询已分配用户角色列表
     *
     * @param query 系统用户信息
     * @param id    角色id
     * @return 已分配系统用户信息列表数据
     */
    List<SysUserDto> selectAllocatedList(SysUserQuery query, Long id);

    /**
     * 查询未分配用户角色列表
     *
     * @param query 系统用户信息
     * @param id    角色id
     * @return 未分配系统用户信息列表数据
     */
    List<SysUserDto> selectUnallocatedList(SysUserQuery query, Long id);

    /**
     * 重置用户密码
     *
     * @param ids 用户id集合
     */
    boolean resetPassword(List<Long> ids);

    /**
     * 修改用户头像
     *
     * @param userId 用户id
     * @param url    头像地址
     */
    boolean updateAvatar(Long userId, String url);

    /**
     * 修改用户信息
     *
     * @param userId 用户id
     * @param passwordUpdate 用户修改密码信息
     */
    boolean updatePassword(Long userId,UserPasswordUpdateVo passwordUpdate);

    /**
     * 修改用户状态
     */
    boolean updateStatus(UserUpdateStatusVo userUpdateStatus);

    /**
     * 用户数据导出
     */
    boolean export(HttpServletResponse response);

    /**
     * 数据导入
     */
    boolean importUserInfo(MultipartFile file);

    /**
     * 封装角色
     * @param users
     */
    void setRole(List<SysUserDto> users);
}
