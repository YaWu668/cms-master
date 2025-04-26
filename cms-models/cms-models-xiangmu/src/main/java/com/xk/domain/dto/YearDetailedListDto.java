package com.xk.domain.dto;

import com.xk.check.annotations.XSSValidation;
import com.xk.domain.query.PageQuery;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 模糊搜索年度组和年度数据模糊搜索
 */
@Data
@Accessors(chain = true)
public class YearDetailedListDto extends PageQuery {
    /**
     * 年度组的名称--模糊搜索
     */
    @XSSValidation(message = "请求包含非法字符")
    private String name;


}
