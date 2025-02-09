package com.xk.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 项目进行解题操作
 */
@Data
@Accessors(chain = true)
public class UpdateProjectPassDto {
    /**
     * 项目id
     */
    @NotNull(message = "项目id不能为空")
    @Min(value = 1, message = "项目id不能小于1")
    private Long projectId;
    /**
     * 项目解题状态设置 3为待结题 4为结题通过 5为结题不通过
     */
    @NotNull(message = "项目解题状态设置不能为null")
    @Min(value = 3, message = "项目解题状态设置不能小于3")
    @Max(value = 5, message = "项目解题状态设置不能大于5")
    private Long Status;
    /**
     * 结题操作备注
     */
    @NotNull(message = "结题备注不能为null")
    @Length(min=5 ,max = 255, message = "备注长度范围为:5~255")
    private String msg;
}
