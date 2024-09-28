package com.cms.system.domain.query;

import com.cms.common.datascope.domain.DataScopeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 角色查询模型
 *
 * @author 邓志军
 * @date 2024年6月4日09:01:25
 */
@ApiModel(description = "角色查询模型")
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRoleQuery extends DataScopeEntity {

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称", position = 1)
    private String roleName;

    /**
     * 数据范围
     */
    @ApiModelProperty(value = "数据范围", position = 2)
    private Integer dataScope;

    /**
     * 角色状态
     */
    @ApiModelProperty(value = "角色状态", position = 3)
    private Integer status;
}
