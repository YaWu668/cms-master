package com.xk.domain.dto;


import com.xk.constant.ProjectConstant;
import com.xk.domain.query.PageQuery;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 项目列表请求通用
 */
@lombok.Data
public class ProjectSelectDto extends PageQuery {
    /**
     * 项目编号
     */
    private String projectNumber;
    /**
     * 项目名字搜索
     */
    private String name;
    /**
     * 项目基本 1:国家级 --字典
     */
    private Long projectRank;
    /**
     * 角色标识符,通过输入的判断是,用户
     */
    private String role;
    /**
     * 项目状态值 ---字典
     */
    private List<Long> states;
    /**
     * 学科类别 1:工科 2:文科  --字典
     */
    private Long subjectCategory;
    /**
     * 老师的id
     */
    private Long teacherId;
    /**
     * 项目类型 ---字典
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
     * 是否分配专家组  true:已分配, false:未分配,为null话,全部查询
     */
    private Boolean isAudit;
    /**
     * 参加项目的人员的id
     */
    private Long memberId;

    /**
     * 批次数据
     */
    private Long yearDataId;

    /**
     * 判断用户输入的角色标识符是否正确
     */
    public boolean isRole() {
        return role.equals(ProjectConstant.ROLE_ADMIN) ||
                role.equals(ProjectConstant.ROLE_SPECIALIST) ||
                role.equals(ProjectConstant.ROLE_COLLEGE) ||
                role.equals(ProjectConstant.ROLE_TEACHER) ||
                role.equals(ProjectConstant.ROLE_STUDENT);
    }

}
