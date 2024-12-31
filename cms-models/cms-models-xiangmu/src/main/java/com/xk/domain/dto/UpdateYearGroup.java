package com.xk.domain.dto;

import lombok.Data;

/**
 * 根据项年度组id进行修改
 */
@Data
public class UpdateYearGroup {
    /**
     * 年度组的id
     */
    private Long yearGroupId;
    /**
     * 年度组的名称
     */
    private String name;
    /**
     * 状态(0正常 1停用)
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;
}
