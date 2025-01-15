package com.xk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xk.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author YaWu
 * @since 2024-12-18 00:29:34
 */
public interface RoleService extends IService<Role> {

    /**
     * 根据传入角色权限字符串判断是否存在角色
     * @param roleKey 角色权限字符串
     * @return 角色是否存在, true:存在, false:不存在
     */
    boolean existRole(String roleKey);

    /**
     * 根据角色权限字符串查询角色信息
     * @param roleKey 角色权限字符串
     * @return 角色信息
     */
    Role selectByRoleKey(String roleKey);

    /**
     * 返回一生一项目管理员的管理员id
     * @return 管理员id
     * @throws Exception
     */
    Long getAdminId() ;

    /**
     * 根据传入的角色标识符,判断当前用户是否有该角色
     * @param role 角色标识符
     * @return 是否有该角色, true:有, false:没有
     */
     boolean hasRole( String role);

    /**
     * 返回学生或非学生角色
     * @param isStudnet 是否学生
     * @return 角色信息
     */
     List<Role> getStudentOrNoStudnet(Boolean isStudnet);

    /**
     *
     */
//    boolean setCollgeOrSpecialistRole(Role );
}

