package com.xk.domain.vo.project;

import lombok.experimental.Accessors;

/**
 * 项目列表vo
 */
@lombok.Data
@Accessors(chain = true)
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class ProjectListvo {
    /**
     * 项目类别 1:一般项目 2:重点
     */
    private Long category;
    /**
     * 项目编号
     */
    private String projectNumber;
    /**
     * 学院组的id
     */
    private Long collegeGroupId;
    /**
     * 学院名称
     */
    private String collegeGroupName;
    /**
     * 项目名称
     */
    private String name;
    /**
     * 负责人的名字
     */
    private String nickName;
    /**
     * 项目id
     */
    private Long projectId;
    /**
     * 项目级别 1:国家级 --字典
     */
    private Long projectRank;
    /**
     * 专家组的id
     */
    private Long specialistGroupId;
    /**
     * 专家组的名称
     */
    private String specialistGroupName;
    /**
     * 项目状态
     */
    private Long state;
    /**
     * 学科类别 1:工科 2:文科  --字典
     */
    private Long subjectCategory;
    /**
     * 项目类型 ---字典 创新XX
     */
    private Long type;
    /**
     * 负责人的id
     */
    private Long userId;
    /**
     * 年度id
     */
    private Long yearGroupId;

    /**
     * 年度名称
     */
    private String yearGroupName;
    /**
     * 年度数据的id
     */
    private Long yearDataId;
    /**
     * 年度数据名称
     */
    private String yearDataName;

}
