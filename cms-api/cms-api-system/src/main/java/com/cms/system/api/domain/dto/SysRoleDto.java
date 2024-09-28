package com.cms.system.api.domain.dto;

import com.cms.system.api.domain.pojo.SysRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

/**
 * 角色表 sys_role
 *
 * @author 邓志军
 * @date 2024年5月28日23:25:32
 */
@ApiModel(description = "角色信息")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysRoleDto extends SysRole implements Serializable {

    @ApiModelProperty(hidden = true)
    private static final long serialVersionUID = 1L;

    /**
     * 用户是否存在此角色标识 默认不存在
     */
    @ApiModelProperty(value = "用户是否存在此角色标识 默认不存在", position = 11)
    private boolean flag = false;

    /**
     * 菜单组
     */
    @ApiModelProperty(value = "菜单组", position = 12)
    private Long[] menuIds;

    /**
     * 部门组（数据权限）
     */
    @ApiModelProperty(value = "部门组（数据权限）", position = 13)
    private Long[] deptIds;

    /**
     * 角色菜单权限
     */
    @ApiModelProperty(value = "角色菜单权限", position = 14)
    private Set<String> permissions;
}
