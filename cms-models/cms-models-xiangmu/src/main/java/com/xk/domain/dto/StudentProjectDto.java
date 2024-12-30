package com.xk.domain.dto;

import com.xk.domain.query.PageQuery;
import lombok.Data;

/**
 * 根据学生的id查询项目
 */
@Data
public class StudentProjectDto extends PageQuery {
    /**
     * 学生的id
     */
    private Long studentId;

}
