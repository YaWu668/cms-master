package com.xk.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class ApplyForDTO{
    /**
     * a,b,c 三选一
     */
    private ApplyForA applyForA;
    /**
     * a,b,c 三选一
     */
    private ApplyForB b;
    /**
     * a,b,c 三选一
     */
    private ApplyForC applyForC;
    /**
     * 详细经费预算, 没有设计业务, 前端发什么样子json, 就存储什么样子
     */
    //禁止null
    @NotNull(message = "详细经费预算不能为空")
    private String budget;
    /**
     * 项目类别 1为一般项目 2为重点支持领域项目
     */
    @NotNull(message = "项目类别不能为空")
    private Long category;
    /**
     * 学院组的id
     */
    @NotNull(message = "学院组的id不能为空")
    private Long collegeGroupId;
    /**
     * 企业老师科研情况，仅创新训练类型项目可为 NULL,可选相
     */
    private String firmTeacherExperience;
    /**
     * 财政拨款的金额(钱)
     */
    @NotNull(message = "财政拨款的金额(钱)不能为空")
    private BigDecimal fiscalAppropriation;
    /**
     * 项目附加的URL地址, 提交只能提交压缩包
     */
    @NotNull(message = "项目附加的URL地址不能为空")
    private String materialsUrl;
    /**
     * 项目名称
     */
    @NotNull(message = "项目名称不能为空")
    private String name;
    /**
     * 负责人曾经参与科研的情况
     */
    @NotNull(message = "负责人曾经参与科研的情况不能为空")
    private String principalExperience;
    /**
     * 项目来源,字典没有定
     */
    @NotNull(message = "项目来源不能为空")
    private Long projectSource;
    /**
     * 项目简介 500字内
     */
    @NotNull(message = "项目简介不能为空")
    private String projectSynopsis;
    /**
     * 项目级别 1为国家级
     */
    @NotNull(message = "项目类型不能为空")
    private Long rank;
    /**
     * 财政拨款的金额
     */
    @NotNull(message = "财政拨款的金额不能为空")
    private BigDecimal schoolAllocation;
    /**
     * 学生人数,第一个必须是负责人,否则抛出异常
     */
    @Valid
    @NotNull(message = "学生报名人数不能为空")
    private List<ApplyForStudent> applyForStudents;
    /**
     * 学科类别 (字典)示例 1为工科 ,  2为文科
     */
    @NotNull(message = "学科类别不能为空")
    private Long subjectCategory;
    /**
     * 指导老师科研情况
     */
    @NotNull(message = "指导老师科研情况不能为空")
    private String teacherExperience;
    /**
     * 老师
     */
    @Valid
    @NotNull(message = "老师报名人数不能为空")
    private List<ApplyForTeacher> applyForTeachers;
    /**
     * 指导老师和企业老师对项目的支持情况, 仅创新训练类型项目是指导老师支持情况
     */
    @NotNull(message = "指导老师和企业老师对项目的支持情况不能为空")
    private String teacherSupport;
    /**
     * 申请金额合计
     */
    @NotNull(message = "申请金额合计不能为空")
    private BigDecimal totalMoney;
    /**
     * 项目类型 1为创新训练项目
     */
    @NotNull(message = "项目类型不能为空")
    private Long type;
    /**
     * 年度数据的id
     */
    @NotNull(message = "年度数据的id不能为空")
    private Long yearDataId;
    /**
     * 年度组的id
     */
    @NotNull(message = "年度组的id不能为空")
    private Long yearGroupId;
    /**
     * 年度类型的id
     */
    @NotNull(message = "年度类型的id不能为空")
    private Long yearTypeId;
}
