package com.cms.system.domain.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.implementation.bind.annotation.Empty;

import javax.validation.constraints.*;
import java.util.List;

/**
 * 角色数据权限
 *
 * @author 邓志军
 * @date 2024年6月7日12:00:31
 */
@ApiModel(description = "角色数据权限")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDateScope {

    /**
     * 角色ID
     */
    @NotNull(message = "角色id不允许为空!!!")
    @ApiModelProperty(value = "角色id", position = 1)
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
     * 数据范围
     */
    @NotBlank(message = "数据范围不能为空")
    @Pattern(regexp = "[1-5]", message = "数据范围只能为1-5")
    @ApiModelProperty(value = "数据范围", position = 4)
    private String dataScope;

    /**
     * 拥有的部门权限
     */
    @ApiModelProperty(value = "拥有的部门权限", position = 5)
    private List<Long> deptIds;
}
