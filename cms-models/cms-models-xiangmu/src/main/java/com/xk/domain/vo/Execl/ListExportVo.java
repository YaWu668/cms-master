package com.xk.domain.vo.Execl;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *根据年度名单导出
 */
@Data
@Accessors(chain = true)
public class ListExportVo {
    /**
     * 序号
     */
    @ExcelProperty(value = "序号", index = 0)
    private Long serialNumber;
    /**
     * 所在学院
     */
    @ExcelProperty(value = "所在学院", index = 1)
    private String college;
    /**
     * 项目编号
     */
    @ExcelProperty(value = "项目编号", index = 2)
    private String projectNumber;
    /**
     * 项目名称
     */
    @ExcelProperty(value = "项目名称", index = 3)
    private String projectName;
    /**
     * 项目类型 1.创新训练项目
     */
    @ExcelProperty(value = "项目类型", index = 4)
    private String projectType;
    /**
     * 项目级别
     */
    @ExcelProperty(value = "项目级别", index = 5)
    private String projectRankStr;
    /**
     * 项目经费
     */
    @ExcelProperty(value = "项目经费", index = 6)
    private String projectFunds;
    /**
     * 项目负责人姓名
     */
    @ExcelProperty(value = "项目负责人姓名", index = 7)
    private String principalName;
    /**
     * 项目负责人学号
     */
    @ExcelProperty(value = "项目负责人学号", index = 8)
    private String principalNumber;
    /**
     * 项目负责人手机
     */
    @ExcelProperty(value = "项目负责人手机", index = 9)
    private String principalPhone;
    /**
     * 项目负责人电子邮箱（可选）
     */
    @ExcelProperty(value = "项目负责人电子邮箱", index = 10)
    private String principalEmail;
    /**
     * 项目其他成员信息
     */
    @ExcelProperty(value = "项目其他成员信息", index = 11)
    private String otherMembers;
    /**
     * 指导教师姓名
     */
    @ExcelProperty(value = "指导教师姓名", index = 12)
    private String teacherName;
    /**
     * 指导教师职称
     */
    @ExcelProperty(value = "指导教师职称", index = 13)
    private String teacherTitle;
    /**
     * 指导老师手机号（可选）
     */
    @ExcelProperty(value = "指导老师手机号", index = 14)
    private String teacherPhone;
    /**
     * 项目期限
     */
    @ExcelProperty(value = "项目期限", index = 15)
    private String projectPeriod;
    /**
     * 所属批次
     */
    @ExcelProperty(value = "所属批次", index = 16)
    private String batch;
    /**
     * 结题状态
     */
    @ExcelProperty(value = "结题状态", index = 17)
    private String state;


    //----下面数据方便数据封装的不用导出操作
    /**
     * 项目类型  (字典)示例: 1为创新训练项目, 2为创业训练项目, 3为创业实践
     */
    private Long type;

    /**
     * 项目级别 (字典)示例 : 1为国家级, 2为区级(省级),3为校级
     */
    private Long projectRank;
    /**
     * 学院组的id
     */
    private Long collegeGroupId;

    /**
     * 年度组的id
     */
    private Long yearGroupId;

    /**
     * 年度数据的id
     */
    private Long yearDataId;

    /**
     * 负责人的id, 确定负责人
     */
    private Long userId;
    /**
     * 项目id
     */
    private Long projectId;
    /**
     * 项目状态
     */
    private Long stateValue;
}
