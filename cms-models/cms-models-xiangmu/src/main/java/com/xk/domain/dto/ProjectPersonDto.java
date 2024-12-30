package com.xk.domain.dto;

import com.xk.domain.query.PageQuery;
import lombok.Data;

/**
 * 条件获取(搜索用户)
 */
@Data
public class ProjectPersonDto extends PageQuery {
    /**
     * true:搜索学生角色) false :搜索学生之外角色
     */
    private Boolean isStudent;
    /**
     * 学号(工号)或者是名字
     */
    private String parameter;
}
