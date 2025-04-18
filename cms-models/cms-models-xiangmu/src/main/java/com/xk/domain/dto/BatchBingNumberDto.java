package com.xk.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.cms.common.core.annotation.excel.ExcelNotation;
import lombok.Data;

/**
 * 批量绑定项目编号
 */
@Data
public class BatchBingNumberDto {
    /**
     * 项目名称
     */
    @ExcelProperty(value = "项目名称",index = 0)
    private String name;
    /**
     * 项目编号
     */
    @ExcelProperty(value = "项目编号",index = 1)
    private String projectNumber;
}
