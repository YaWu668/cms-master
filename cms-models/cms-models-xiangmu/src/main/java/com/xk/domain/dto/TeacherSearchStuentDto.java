package com.xk.domain.dto;

import com.xk.domain.query.PageQuery;
import lombok.Data;

/**
 * 老师搜索自己绑定学生的项目
 */
@Data
public class TeacherSearchStuentDto extends PageQuery {
    /**
     * 学号搜索
     */
    private String userName;

    /**
     * 名字
     */
    private String nickName;

}
