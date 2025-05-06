package com.cms.system.api.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.cms.system.api.domain.pojo.SysUser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 用户对象 sys_user
 *
 * @author 邓志军
 * @date 2024年5月28日23:25:32
 */
@JsonIgnoreProperties({"createTime"})
@ApiModel(description = "用户对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysUserDto extends SysUser {

    @ApiModelProperty(hidden = true)
    private static final long serialVersionUID = 1L;

    /**
     * 最后登录时间
     */
    @ApiModelProperty(value = "最后登录时间", position = 14)
    private Date loginDate;

    /**
     * 部门对象
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "部门对象", position = 15)
    private SysDeptDto dept;

    /**
     * 角色对象
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "角色对象", position = 16)
    private List<SysRoleDto> roles;

    /**
     * 角色组
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "角色组", position = 17)
    private List<Long> roleIds;

    /**
     * 岗位组
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "岗位组", position = 18)
    private List<Long> postIds;

    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID", position = 19)
    private Long roleId;
    /**
     * 角色key
     */
    @TableField(exist = false)
    private List<String> roleKey;
    /**
     * 角色名字
     */
    @TableField(exist = false)
    private List<String> roleName;
}
