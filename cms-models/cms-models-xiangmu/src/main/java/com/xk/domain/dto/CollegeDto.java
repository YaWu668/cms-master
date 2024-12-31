package com.xk.domain.dto;

import com.xk.domain.query.PageQuery;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 学院条件查询
 */
@Data
public class CollegeDto extends PageQuery {
    /**
     * 学院名称
     */

    private String name;
}
