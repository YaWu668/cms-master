package com.cms.system.api.model;

import com.cms.system.api.domain.dto.SysUserDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;


/**
 * 用户信息
 *
 * @author 邓志军
 * @date 2024年5月29日01:11:05
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "用户信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements Serializable {

    @ApiModelProperty(hidden = true)
    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一标识
     */
    @ApiModelProperty(value = "用户唯一标识", position = 2)
    private String token;

    /**
     * 用户名id
     */
    @ApiModelProperty(value = "用户名id", position = 3)
    private Long userid;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", position = 4)
    private String username;

    /**
     * 登录时间
     */
    @ApiModelProperty(value = "登录时间", position = 5)
    private Long loginTime;

    /**
     * 过期时间
     */
    @ApiModelProperty(value = "过期时间", position = 6)
    private Long expireTime;

    /**
     * 登录IP地址
     */
    @ApiModelProperty(value = "登录IP地址", position = 7)
    private String ipaddr;

    /**
     * 权限列表
     */
    @ApiModelProperty(value = "权限列表", position = 8)
    private Set<String> permissions;

    /**
     * 角色列表
     */
    @ApiModelProperty(value = "角色列表", position = 9)
    private Set<String> roles;

    /**
     * 用户信息
     */
    @ApiModelProperty(value = "用户信息", position = 10)
    private SysUserDto sysUser;
}
