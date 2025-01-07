package com.xk.domain.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 项目审核dto
 */
@lombok.Data
public class ProjectAuditDto {
    /**
     * 审核意见
     */
    @NotNull(message = "审核意见不能为空")
    @Length(min = 4, max = 1000, message = "审核意见长度必须在4-1000之间")
    private String auditOpinion;
    /**
     * 审核状态(0表示通过, 1表示不通过)
     */
    @NotNull(message = "审核状态不能为空")
    @Min(value = 0, message = "审核状态只能为0或1")
    @Max(value = 1, message = "审核状态只能为0或1")
    private Long auditState;
    /**
     * 项目的id
     */
    @NotNull(message = "项目的id不能为空")
    @Min(value = 1, message = "项目的id只能为正整数")
    private Long projectId;
}
