package com.xk.domain.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 根据项年度组id进行修改
 */
@Data
public class UpdateYearGroup {
    /**
     * 年度组的id
     */
    @NotNull(message = "年度组id不能为空")
    private Long yearGroupId;
    /**
     * 年度组的名称
     */
    @NotNull(message = "年度组名称不能为空")
    @NotEmpty(message = "年度组名称不能为空")
    @Length(min = 5,max = 30, message = "年度组名字长度:5~30")
    private String name;
    /**
     * 状态(0正常 1停用)
     */
    @NotNull(message = "状态值不能为空")
    @Min(value = 0,message = "状态值不能小于0")
    @Max(value = 1,message = "状态值不能大于1")
    private Integer status;
    /**
     * 备注
     */
    private String remark;
}
