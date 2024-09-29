package com.cms.system.domain.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 系统字典类型列表查询模型
 *
 * @author 邓志军
 * @date 2024年5月30日19:04:35
 */
@ApiModel(description = "系统字典类型列表查询模型")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysDictTypeQuery {

    /**
     * 字典名称
     */
    @ApiModelProperty(value = "字典名称", position = 1)
    private String dictName;

    /**
     * 字典类型
     */
    @ApiModelProperty(value = "字典类型", position = 2)
    private String dictType;

    /**
     * 用户状态
     */
    @ApiModelProperty(value = "用户状态", position = 3)
    private Integer status;

    /**
     * 访问时间开始时间
     */
    @ApiModelProperty(value = "访问时间开始时间", position = 4)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;

    /**
     * 访问时间结束时间
     */
    @ApiModelProperty(value = "访问时间结束时间", position = 5)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
}
