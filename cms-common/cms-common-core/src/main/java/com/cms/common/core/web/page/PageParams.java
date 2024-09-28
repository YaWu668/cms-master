package com.cms.common.core.web.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 分页查询参数
 *
 * @author 邓志军
 * @date 2024-05-28
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PageParams {
    /*
     * 当前页码
     */
    private Integer current = 1;

    /*
     * 每页显示的记录数
     */
    private Integer limit = 10;
}
