package com.cms.system.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 部门查询
 *
 * @author 邓志军
 * @date 2024年6月8日08:45:39
 */
@ApiModel(description = "部门查询")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysDeptQuery implements Serializable {
    @ApiModelProperty(hidden = true)
    private static final long serialVersionUID = 1L;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称", position = 2)
    private String deptName;

    /**
     * 部门状态
     */
    @ApiModelProperty(value = "部门状态", position = 3)
    private Integer status;

}
