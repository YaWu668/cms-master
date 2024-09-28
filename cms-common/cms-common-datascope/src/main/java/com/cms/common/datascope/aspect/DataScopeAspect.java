package com.cms.common.datascope.aspect;

import com.cms.common.core.context.SecurityContextHolder;
import com.cms.common.core.text.Convert;
import com.cms.common.core.utils.StringUtils;
import com.cms.common.datascope.annotation.DataScope;
import com.cms.common.datascope.domain.DataScopeEntity;
import com.cms.common.security.utils.SecurityUtils;
import com.cms.system.api.domain.dto.SysRoleDto;
import com.cms.system.api.domain.dto.SysUserDto;
import com.cms.system.api.model.LoginUser;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据过滤处理
 *
 * @author 邓志军
 * @date 2024年6月3日20:13:40
 */
@Aspect
@Component
public class DataScopeAspect {

    /**
     * 全部数据权限
     */
    public static final String DATA_SCOPE_ALL = "1";

    /**
     * 自定数据权限
     */
    public static final String DATA_SCOPE_CUSTOM = "2";

    /**
     * 部门数据权限
     */
    public static final String DATA_SCOPE_DEPT = "3";

    /**
     * 部门及以下数据权限
     */
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";

    /**
     * 仅本人数据权限
     */
    public static final String DATA_SCOPE_SELF = "5";

    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE = "dataScope";

    // 在方法执行前执行，根据注解进行数据范围处理,切点为 @DataScope 注解
    @Before("@annotation(controllerDataScope)")
    public void doBefore(JoinPoint point, DataScope controllerDataScope) {
        this.clearDataScope(point);
        this.handleDataScope(point, controllerDataScope);
    }

    /**
     * 处理数据权限，根据当前用户的权限信息进行数据过滤
     *
     * @param joinPoint           切点
     * @param controllerDataScope 控制器上的数据权限注解
     */
    private void handleDataScope(final JoinPoint joinPoint, DataScope controllerDataScope) {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();

        // 检查当前用户是否存在
        if (StringUtils.isNotNull(loginUser)) {
            // 获取当前登录的系统用户信息
            SysUserDto currentUser = loginUser.getSysUser();

            // 如果当前用户不为空且不是超级管理员，则进行数据权限过滤,超级管理员没有校验的必要
            if (StringUtils.isNotNull(currentUser) && !SecurityUtils.isAdmin(currentUser.getUserId())) {
                // 获取权限注解中定义的权限值，如果未设置则使用当前用户的权限
                String permission = StringUtils.defaultIfEmpty(controllerDataScope.permission(), SecurityContextHolder.getPermission());

                // 数据权限过滤
                DataScopeAspect.dataScopeFilter(joinPoint, currentUser, controllerDataScope.deptAlias(),
                        controllerDataScope.userAlias(), permission);
            }
        }
    }

    /**
     * 数据范围过滤，根据用户的角色数据权限进行数据过滤
     *
     * @param joinPoint  切点，用于获取方法参数等信息
     * @param user       用户信息，包含用户角色等数据权限信息
     * @param deptAlias  部门别名，用于构建 SQL 语句中的部门字段别名
     * @param userAlias  用户别名，用于构建 SQL 语句中的用户字段别名
     * @param permission 权限字符，用于过滤权限
     */
    public static void dataScopeFilter(JoinPoint joinPoint, SysUserDto user, String deptAlias, String userAlias, String permission) {
        // 构建 SQL 语句
        StringBuilder sqlString = new StringBuilder();
        // 存储已处理的数据权限条件，用于去重
        List<String> conditions = new ArrayList<>();

        // 遍历用户的角色列表，根据角色的数据权限配置构建数据范围过滤条件
        for (SysRoleDto role : user.getRoles()) {
            String dataScope = role.getDataScope();
            // 如果当前数据权限条件已处理过，则跳过
            if (!DATA_SCOPE_CUSTOM.equals(dataScope) && conditions.contains(dataScope)) {
                continue;
            }
            // 如果传入的权限字符非空且当前角色不包含该权限字符，则跳过
            if (StringUtils.isNotEmpty(permission) && StringUtils.isNotEmpty(role.getPermissions())
                    && !StringUtils.containsAny(role.getPermissions(), Convert.toStrArray(permission))) {
                continue;
            }
            // 根据角色的数据权限类型构建相应的 SQL 过滤条件
            if (DATA_SCOPE_ALL.equals(dataScope)) {
                // 数据权限为所有数据时，不添加任何过滤条件
                sqlString = new StringBuilder();
                conditions.add(dataScope);
                break;
            } else if (DATA_SCOPE_CUSTOM.equals(dataScope)) {
                // 数据权限为自定义时，根据角色关联的部门表构建部门 ID 过滤条件
                sqlString.append(StringUtils.format(
                        " or {}.dept_id in ( select dept_id from sys_role_dept where role_id = {} ) ", deptAlias,
                        role.getRoleId()));
            } else if (DATA_SCOPE_DEPT.equals(dataScope)) {
                // 数据权限为所在部门时，直接根据用户所属部门 ID 过滤
                sqlString.append(StringUtils.format(" or {}.dept_id = {} ", deptAlias, user.getDeptId()));
            } else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope)) {
                // 数据权限为所在部门及其子部门时，根据部门表中的祖先字段过滤
                sqlString.append(StringUtils.format(
                        " OR {}.dept_id IN ( select dept_id from sys_dept where dept_id = {} or find_in_set( {} , ancestors ) )",
                        deptAlias, user.getDeptId(), user.getDeptId()));
            } else if (DATA_SCOPE_SELF.equals(dataScope)) {
                // 数据权限为仅本人时，根据用户 ID 过滤
                if (StringUtils.isNotBlank(userAlias)) {
                    sqlString.append(StringUtils.format(" or {}.user_id = {} ", userAlias, user.getUserId()));
                } else {
                    // 数据权限为仅本人且没有指定用户别名时，不查询任何数据
                    sqlString.append(StringUtils.format(" or {}.dept_id = 0 ", deptAlias));
                }
            }
            // 将当前数据权限条件添加到已处理列表中
            conditions.add(dataScope);
        }

        // 如果所有角色都不包含传入的权限字符，则不查询任何数据
        if (StringUtils.isEmpty(conditions)) {
            sqlString.append(StringUtils.format(" or {}.dept_id = 0 ", deptAlias));
        }

        // 将构建好的 SQL 过滤条件应用到方法参数中，以实现数据范围过滤
        if (StringUtils.isNotBlank(sqlString.toString())) {
            Object params = joinPoint.getArgs()[0];
            if (StringUtils.isNotNull(params) && params instanceof DataScopeEntity) {
                DataScopeEntity dataScope = (DataScopeEntity) params;
                dataScope.getParams().put(DATA_SCOPE, " and (" + sqlString.substring(4) + ")");
            }
        }
    }

    /**
     * 在拼接权限SQL语句之前，先清空params.dataScope参数，以防止注入攻击。
     *
     * @param joinPoint 切点，用于获取方法参数等信息
     */
    private void clearDataScope(final JoinPoint joinPoint) {
        // 获取方法参数
        Object params = joinPoint.getArgs()[0];

        // 判断参数是否为 DataScopeEntity 类型
        if (StringUtils.isNotNull(params) && params instanceof DataScopeEntity) {
            // 强制转换为 DataScopeEntity 类型
            DataScopeEntity dataScope = (DataScopeEntity) params;

            // 清空dataScope对象中的params中的DATA_SCOPE参数，防止注入攻击
            dataScope.getParams().put(DATA_SCOPE, "");
        }
    }
}
