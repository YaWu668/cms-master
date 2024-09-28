package com.cms.common.core.web.page;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询结果模型类
 *
 * @author 邓志军
 * @date 2024-05-28
 */
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TableDataInfo<T> implements Serializable {
    /**
     * 总记录数
     */
    private Long total;

    /**
     * 总页数
     */
    private Integer pages;

    /**
     * 当前第几页
     */
    private Integer current;

    /**
     * 每页记录数
     */
    private Integer size;

    /**
     * 结果集
     */
    private List<T> records;
}
