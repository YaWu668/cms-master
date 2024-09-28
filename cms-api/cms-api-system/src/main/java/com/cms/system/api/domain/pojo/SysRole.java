package com.cms.system.api.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.cms.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
public class SysRole extends BaseEntity {

    @ApiModelProperty(hidden = true)
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "角色ID", position = 2)
    private Long roleId;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称", position = 3)
    private String roleName;

    /**
     * 角色权限
     */
    @ApiModelProperty(value = "角色权限", position = 4)
    private String roleKey;

    /**
     * 角色排序
     */
    @ApiModelProperty(value = "角色排序", position = 5)
    private Integer roleSort;

    /**
     * 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限；5：仅本人数据权限）
     */
    @ApiModelProperty(value = "数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限；5：仅本人数据权限）", position = 6)
    private String dataScope;

    /**
     * 菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示）
     */
    @ApiModelProperty(value = "菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示）", position = 7)
    private boolean menuCheckStrictly;

    /**
     * 部门树选择项是否关联显示（0：父子不互相关联显示 1：父子互相关联显示 ）
     */
    @ApiModelProperty(value = "部门树选择项是否关联显示（0：父子不互相关联显示 1：父子互相关联显示 ）", position = 8)
    private boolean deptCheckStrictly;

    /**
     * 角色状态（0正常 1停用）
     */
    @ApiModelProperty(value = "角色状态（0正常 1停用）", position = 9)
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）", position = 10)
    private String delFlag;
}
