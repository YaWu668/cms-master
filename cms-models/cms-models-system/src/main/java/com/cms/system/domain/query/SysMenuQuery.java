package com.cms.system.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜单查询模型
 *
 * @author 邓志军
 * @date 2024年6月4日18:11:26
 */
@ApiModel(description = "菜单查询模型")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysMenuQuery {

    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称", position = 1)
    private String menuName;

    /**
     * 菜单权限
     */
    @ApiModelProperty(value = "菜单权限", position = 2)
    private Integer status;
}
