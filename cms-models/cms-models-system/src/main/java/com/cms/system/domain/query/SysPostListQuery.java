package com.cms.system.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 岗位查询对象
 *
 * @author 邓志军
 * @date 2024年6月7日15:12:12
 */
@ApiModel(description = "岗位查询对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysPostListQuery {

    /**
     * 岗位编码
     */
    @ApiModelProperty(value = "岗位编码", position = 1)
    private String postCode;

    /**
     * 岗位名称
     */
    @ApiModelProperty(value = "岗位名称", position = 2)
    private String postName;

    /**
     * 岗位类型
     */
    @ApiModelProperty(value = "岗位类型", position = 3)
    private String postType;

    /**
     * 状态（0正常 1停用）
     */
    @ApiModelProperty(value = "状态（0正常 1停用）", position = 4)
    private String status;
}
