package com.cms.common.core.utils;

import com.cms.common.core.utils.sql.SqlUtil;
import com.cms.common.core.web.page.PageDomain;
import com.cms.common.core.web.page.TableSupport;
import com.github.pagehelper.PageHelper;

/**
 * 分页工具类
 * 该工具类用于在进行分页查询时设置分页参数，并提供了清理分页参数的方法。
 * 通过调用 startPage() 方法设置分页参数，调用 clearPage() 方法清理分页参数。
 * 注意：该工具类继承自 PageHelper，用于方便集成 PageHelper 分页功能。
 *
 * @author 邓志军
 * @date 2024年5月30日21:14:36
 */
public class PageUtils extends PageHelper {
    /**
     * 设置请求分页数据
     * 根据请求中的分页参数，设置 PageHelper 分页插件的分页参数，并启用合理化参数。
     * 合理化参数用于自动校正 pageNum，防止超出页数范围。
     */
    public static void startPage() {
        // 获取请求中的分页参数
        PageDomain pageDomain = TableSupport.getPageDomain();
        // 获取页码
        Integer pageNum = pageDomain.getPageNum();
        // 获取每页条数
        Integer pageSize = pageDomain.getPageSize();
        // 获取排序字段
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
        // 获取是否启用合理化参数
        Boolean reasonable = pageDomain.getReasonable();
        // 使用 PageHelper.startPage 方法设置分页参数，并设置合理化参数
        PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
    }

    /**
     * 清理分页的线程变量
     * 清理 PageHelper 在当前线程中设置的分页参数，防止线程重用导致分页参数污染。
     */
    public static void clearPage() {
        PageHelper.clearPage(); // 清理分页参数
    }
}
