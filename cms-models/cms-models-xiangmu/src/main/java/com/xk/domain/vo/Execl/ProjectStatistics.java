package com.xk.domain.vo.Execl;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 项目统计 DTO：封装一次性查询得到的 8 个统计字段
 */
@Data
public class ProjectStatistics {
    /**
     * 年度
     */
    @ExcelProperty(value = "年度",index = 0)
    private String yearly;

    /**
     * 全部项目_总经费
     */
    @ExcelProperty(value = "全部项目_总经费",index = 1)
    private String totalMoneyAll;

    /**
     * 全部项目_参与人数
     */
    @ExcelProperty(value = "全部项目_参与人数",index = 2)
    private String participantsAll;

    /**
     * 全部项目_项目数量
     */
    @ExcelProperty(value = "全部项目_项目数量",index = 3)
    private String validProjectsAll;

    /**
     * 创新类_项目数量
     */
    @ExcelProperty(value = "创新类_项目数量",index = 4)
    private String validProjectsInnovation;

    /**
     * 创新类_参与人数
      */
    @ExcelProperty(value = "创新类_参与人数",index = 5)
    private String participantsInnovation;

    /**
     * 创业类_项目数量
     */
    @ExcelProperty(value = "创业类_项目数量",index = 6)
    private String validProjectsEntrepreneurship;

    /**
     * 创业类_参与人数
     */
    @ExcelProperty(value = "创业类_参与人数",index = 7)
    private String participantsEntrepreneurship;

    /**
     * 创业类_总经费
     */
    @ExcelProperty(value = "创业类_总经费",index = 8)
    private String totalMoneyEntrepreneurship;
}
