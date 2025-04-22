package com.xk.domain.vo.Execl;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ExportProjectNumberExeclVo {
    /**
     * 序号
     */
    @ExcelProperty(value = "序号", index = 0)
    private Long serialNumber;
    /**
     * 项目编号
     */
    @ExcelProperty(value = "项目编号", index = 1)
    private String projectNumber;
    /**
     * 项目名称
     */
    @ExcelProperty(value = "项目名称", index = 2)
    private String projectName;
    /**
     * 项目类型 1.创新训练项目
     */
    @ExcelProperty(value = "项目类型", index = 3)
    private String projectType;
    /**
     * 项目负责人姓名
     */
    @ExcelProperty(value = "项目负责人姓名", index = 4)
    private String principalName;
    /**
     * 项目负责人学号
     */
    @ExcelProperty(value = "项目负责人学号", index = 5)
    private String principalNumber;
    /**
     * 项目其他成员信息
     */
    @ExcelProperty(value = "项目其他成员信息", index = 6)
    private String otherMembers;
    /**
     * 指导教师姓名
     */
    @ExcelProperty(value = "指导教师姓名", index = 7)
    private String teacherName;
    /**
     * 项目期限
     */
    @ExcelProperty(value = "项目期限", index = 8)
    private String projectPeriod;
    /**
     * 所属批次
     */
    @ExcelProperty(value = "所属批次", index = 9)
    private String batch;
    /**
     * 评审结果
     */
    @ExcelProperty(value = "评审结果", index = 10)
    private String state;
    /**
     * 学分
     */
    @ExcelProperty(value = "学分", index = 11)
    private String credits;
}
