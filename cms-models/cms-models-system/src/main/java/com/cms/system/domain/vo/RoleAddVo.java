package com.cms.system.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 角色添加视图对象
 *
 * @author 邓志军
 * @date 2024年6月3日22:08:29
 */
@ApiModel(description = "角色添加视图对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleAddVo {

    @TableId
    private Long roleId;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 30, message = "角色名称长度不能超过30个字符")
    @ApiModelProperty(value = "角色名称", position = 1)
    private String roleName;

    /**
     * 角色权限字符
     */
    @NotBlank(message = "权限字符不能为空")
    @Size(max = 100, message = "权限字符长度不能超过100个字符")
    @ApiModelProperty(value = "角色权限字符", position = 2)
    private String roleKey;

    /**
     * 角色排序
     */
    @NotNull(message = "显示顺序不能为空")
    @ApiModelProperty(value = "角色排序", position = 3)
    private Integer roleSort;

    /**
     * 角色状态（0正常 1停用）
     */
    @ApiModelProperty(value = "角色状态（0正常 1停用）", position = 4)
    private String status;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注内容长度不能超过500个字符")
    @ApiModelProperty(value = "备注", position = 5)
    private String remark;
}
