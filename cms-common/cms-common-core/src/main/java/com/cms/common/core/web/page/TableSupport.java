package com.cms.common.core.web.page;


import com.cms.common.core.text.Convert;
import com.cms.common.core.utils.ServletUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 表格数据处理
 *
 * @author 邓志军
 * @date 2024年5月30日21:17:22
 */
@Data
@AllArgsConstructor
public class TableSupport {
    /**
     * 当前记录起始索引
     */
    public static final String CURRENT = "current";

    /**
     * 每页显示记录数
     */
    public static final String LIMIT = "limit";

    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String IS_ASC = "isAsc";

    /**
     * 分页参数合理化
     */
    public static final String REASONABLE = "reasonable";

    /**
     * 封装分页对象
     *
     * @return 包含分页参数的PageDomain对象
     */
    public static PageDomain getPageDomain() {
        // 创建一个新的PageDomain对象
        PageDomain pageDomain = new PageDomain();
        // 设置当前页码，默认为1
        pageDomain.setPageNum(Convert.toInt(ServletUtils.getParameter(CURRENT), 1));
        // 设置每页记录数，默认为10
        pageDomain.setPageSize(Convert.toInt(ServletUtils.getParameter(LIMIT), 10));
        // 设置排序字段
        pageDomain.setOrderByColumn(ServletUtils.getParameter(ORDER_BY_COLUMN));
        // 设置排序方式（升序或降序）
        pageDomain.setIsAsc(ServletUtils.getParameter(IS_ASC));
        // 设置是否启用合理化参数，默认为false
        pageDomain.setReasonable(ServletUtils.getParameterToBool(REASONABLE));
        // 返回封装好分页参数的PageDomain对象
        return pageDomain;
    }
}
