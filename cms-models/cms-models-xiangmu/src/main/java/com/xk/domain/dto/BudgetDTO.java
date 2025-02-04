package com.xk.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BudgetDTO {

    /**
     * 详细经费预算, 没有设计业务, 前端发什么样子json, 就存储什么样子
     */
    @NotNull(message = "详细经费预算不能为空")
    @NotEmpty(message = "详细经费预算不能为空")
    private String budget;
    /**
     * 财政拨款的金额(钱)
     */
    @NotNull(message = "财政拨款的金额(钱)不能为空")
    @Pattern(regexp = "^-?([0-9]+|[0-9]{1,3}(,[0-9]{3})*)(.[0-9]{1,2})?$" ,message = "财政拨款的金额格式不正确")
    @NotEmpty(message = "财政拨款的金额不能为空")
    private String fiscalAppropriation;
    /**
     * 学校拨款的金额
     */
    @NotNull(message = "财政拨款的金额不能为空")
    @Pattern(regexp = "^-?([0-9]+|[0-9]{1,3}(,[0-9]{3})*)(.[0-9]{1,2})?$" ,message = "财政拨款的金额格式不正确")
    @NotEmpty(message = "财政拨款的金额不能为空")
    private String schoolAllocation;
    /**
     * 申请金额合计
     */
    @NotNull(message = "申请金额合计不能为空")
    @Pattern(regexp = "^-?([0-9]+|[0-9]{1,3}(,[0-9]{3})*)(.[0-9]{1,2})?$" ,message = "申请金额合计格式不正确")
    @NotEmpty(message = "申请金额合计不能为空")
    private String totalMoney;
}
