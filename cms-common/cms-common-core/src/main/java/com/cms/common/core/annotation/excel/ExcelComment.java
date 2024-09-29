package com.cms.common.core.annotation.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Excel导出批注实体
 *
 * @author 邓志军
 * @data 2024年9月5日11:00:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelComment {
    /**
     * 列号
     */
    private Integer column;

    /**
     * 批注值
     */
    private String remarkValue;

    /**
     * 批注行高
     */
    int remarkRowHigh;

    /**
     * 批注列宽
     */
    int remarkColumnWide;

    /**
     * 批注所在行
     */
    int row;
}
