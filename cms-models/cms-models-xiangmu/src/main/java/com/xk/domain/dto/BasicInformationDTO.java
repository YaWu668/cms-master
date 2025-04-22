package com.xk.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xk.check.annotations.RichText;
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

/**
 * 基本情况校验的实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BasicInformationDTO {
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
    @RichText
    private String firmTeacherExperience;
    /**
     * 项目名称
     */
    @NotNull(message = "项目名称不能为空")
    @NotEmpty(message = "项目名称不能为空")
    @RichText
    @Pattern(regexp = "[\\\\\\\\/:*?\\\"<>|]" ,message = "项目名称格式不正确,下禁止的字符: \\ / : * ? \" < > |")
    private String name;
    /**
     * 负责人曾经参与科研的情况
     */
    @NotNull(message = "负责人曾经参与科研的情况不能为空")
    @NotEmpty(message = "负责人曾经参与科研的情况不能为空")
    @RichText
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
    @NotEmpty(message = "项目简介不能为空")
    @RichText
    private String projectSynopsis;
    /**
     * 项目级别 1为国家级
     */
    @NotNull(message = "项目类型不能为空")
    private Long projectRank;
    /**
     * 学科类别 (字典)示例 1为工科 ,  2为文科
     */
    @NotNull(message = "学科类别不能为空")
    private Long subjectCategory;
    /**
     * 指导老师科研情况
     */
    @NotNull(message = "指导老师科研情况不能为空")
    @NotEmpty(message = "指导老师科研情况不能为空")
    @RichText
    private String teacherExperience;
    /**
     * 指导老师和企业老师对项目的支持情况, 仅创新训练类型项目是指导老师支持情况
     */
    @NotNull(message = "指导老师和企业老师对项目的支持情况不能为空")
    @NotEmpty(message = "指导老师和企业老师对项目的支持情况不能为空")
    @RichText
    private String teacherSupport;
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
}
