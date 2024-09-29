package com.cms.system.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 系统字典数据查询模型
 *
 * @author 邓志军
 * @date 2024年6月1日21:19:00
 */
@ApiModel(description = "系统字典数据查询模型")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysDictDataQuery implements Serializable {
    /**
     * 字典标签
     */
    @ApiModelProperty(value = "字典标签", position = 1)
    private String dictLabel;

    /**
     * 字典状态
     */
    @ApiModelProperty(value = "字典状态", position = 2)
    private String status;
}
