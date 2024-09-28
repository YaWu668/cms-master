package com.cms.system.domain.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 系统参数查询参数
 */
@ApiModel(description = "系统参数查询参数")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SysConfigQuery {

    /**
     * 参数名称
     */
    private String configName;

    /**
     * 系统内置（Y是 N否）
     */
    private String configType;

    /**
     * 创建时间开始
     */
    @ApiModelProperty(value = "访问时间开始时间", position = 4)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;

    /**
     * 创建时间结束
     */
    @ApiModelProperty(value = "创建时间结束", position = 5)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
}
