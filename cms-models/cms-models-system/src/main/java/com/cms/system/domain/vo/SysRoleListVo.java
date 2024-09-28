package com.cms.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统角色列表 Vo 模型
 *
 * @author 邓志军
 * @date 2024年6月4日09:26:34
 */
@ApiModel(description = "系统角色列表 Vo 模型")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysRoleListVo {
    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID", position = 1)
    private Long roleId;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称", position = 2)
    private String roleName;

    /**
     * 角色权限
     */
    @ApiModelProperty(value = "角色权限", position = 3)
    private String roleKey;

    /**
     * 显示排序
     */
    @ApiModelProperty(value = "显示排序", position = 4)
    private Integer roleSort;

    /**
     * 人员数
     */
    @ApiModelProperty(value = "人员数", position = 5)
    private Integer userCount;

    /**
     * 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限；5：仅本人数据权限）
     */
    @ApiModelProperty(value = "数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限；5：仅本人数据权限）", position = 6)
    private String dataScope;

    /**
     * 角色状态（0正常 1停用）
     */
    @ApiModelProperty(value = "角色状态（0正常 1停用）", position = 7)
    private String status;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", position = 8)
    private String remark;
}
