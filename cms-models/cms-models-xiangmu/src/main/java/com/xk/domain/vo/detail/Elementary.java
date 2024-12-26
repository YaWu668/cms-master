package com.xk.domain.vo.detail;

import lombok.experimental.Accessors;

/**
 * 基本情况
 */
@lombok.Data
@Accessors(chain = true)
public class Elementary {
    /**
     * 项目立项时间 (yyyy-MM-dd HH:mm:ss)
     */
    private String beginTime;
    /**
     * 项目类别,(xm_item_category) 1为一般项目
     */
    private Long category;
    /**
     * 学院组的id(缺少api,根据id获取学院名称)--
     */
    private Long collegeGroupId;
    /**
     * 项目结题时间 (yyyy-MM-dd HH:mm:ss)
     */
    private String endTime;
    /**
     * 企业老师科研情况，仅创新训练类型项目可为 NULL,可选相
     */
    private String firmTeacherExperience;
    /**
     * 项目名称
     */
    private String name;
    /**
     * 负责人曾经参与科研的情况
     */
    private String principalExperience;
    /**
     * 项目级别, (字典标识符:xm_item_rank) 1为国家级
     */
    private Long projectRank;
    /**
     * 项目来源(xm_item_project_source) 1.竞赛专项
     */
    private Long projectSource;
    /**
     * 项目简介 500字内
     */
    private String projectSynopsis;
    /**
     * 学科类别(字典标识符:xm_item_subject_category) 1为工科
     */
    private Long subjectCategory;
    /**
     * 指导老师科研情况
     */
    private String teacherExperience;
    /**
     * 指导老师和企业老师对项目的支持情况, 仅创新训练类型项目是指导老师支持情况
     */
    private String teacherSupport;
    /**
     * 项目类型, (字典标识符:xm_item_type) 1为创新训练项目
     */
    private Long type;
    /**
     * 年度数据的id(缺少api根据id)--- 2024年度
     */
    private Long yearDataId;
    /**
     * 年度组的id(缺少api根据id)--- 一年期
     */
    private Long yearGroupId;
}
