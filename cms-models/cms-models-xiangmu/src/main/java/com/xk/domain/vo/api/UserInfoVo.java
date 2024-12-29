package com.xk.domain.vo.api;

import com.cms.system.api.domain.dto.SysUserDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * 用户信息视图对象
 *
 * @author 邓志军
 * @date 2024年5月29日20:53:08
 */
@ApiModel(description = "用户信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVo {

    /**
     * 用户信息
     */
    @ApiModelProperty(value = "用户信息", position = 1)
    private SysUserDto user;

    /**
     * 用户角色id集合
     */
    private List<Long> roleIds;

    /**
     * 用户岗位集合
     */
    private List<Long> postIds;

    /**
     * 角色集合
     */
    @ApiModelProperty(value = "角色集合", position = 2)
    private Set<String> roles;

    /**
     * 权限集合
     */
    @ApiModelProperty(value = "权限集合", position = 3)
    private Set<String> permissions;

    public UserInfoVo(SysUserDto user, List<Long> roleIds, List<Long> postIds) {
        this.user = user;
        this.roleIds = roleIds;
        this.postIds = postIds;
    }

    public UserInfoVo(SysUserDto user, Set<String> roles, Set<String> permissions) {
        this.user = user;
        this.roles = roles;
        this.permissions = permissions;
    }
}
