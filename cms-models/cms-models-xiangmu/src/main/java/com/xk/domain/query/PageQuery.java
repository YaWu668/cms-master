package com.xk.domain.query;

import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cms.common.core.exception.ServiceException;
import lombok.Data;

import javax.validation.constraints.Min;
import java.util.Arrays;
import java.util.List;
/**
 * 分页查询参数对象
 * 提供分页参数封装和 MyBatis-Plus 分页对象转换功能。
 */
@Data
public class PageQuery {

    /**
     * 当前页码，默认值为 1，必须大于等于 1。
     */
    @Min(value = 1, message = "页码必须大于等于1")
    private Long pageNo ;

    /**
     * 每页条数，默认值为 10，必须大于等于 1。
     */

    @Min(value = 1, message = "每页条数必须大于等于1")
    private Long pageSize ;

    /**
     * 排序字段。
     */
    private String sortBy;

    /**
     * 排序方式：true 表示升序，false 表示降序。
     * 默认为 false（降序）。
     */
    private Boolean isAsc;

    /**
     * 转换为 MyBatis-Plus 的分页对象，支持动态排序。
     *
     * @param <T>    数据类型
     * @param orders 手动指定的排序规则（可选）
     * @return MyBatis-Plus 分页对象
     */
    public <T> Page<T> toMpPage(OrderItem... orders) {
        setDefaultsIfNull();
        if(!isValid() ){
            throw  new ServiceException("分页参数不合法");
        }
        Page<T> p = Page.of(pageNo, pageSize);
        if (StringUtils.isNotBlank(sortBy) && isSortByValid(sortBy)) {
            p.addOrder(new OrderItem(sortBy, getIsAscOrDefault()));
        } else if (orders != null) {
            p.addOrder(orders);
        }
        return p;
    }

    /**
     * 使用指定默认排序生成 MyBatis-Plus 的分页对象。
     *
     * @param <T>          数据类型
     * @param defaultSortBy 默认排序字段
     * @param defaultAsc    默认排序方式
     * @return MyBatis-Plus 分页对象
     */
    public <T> Page<T> toMpPageWithDefaultSort(String defaultSortBy, boolean defaultAsc) {
        // 如果未指定排序字段，则使用默认排序字段
        if (StringUtils.isBlank(sortBy)) {
            this.sortBy = defaultSortBy;
            this.isAsc = defaultAsc;
        }
        return toMpPage();
    }

    /**
     * 使用创建时间降序排序生成分页对象。
     *
     * @param <T> 数据类型
     * @return MyBatis-Plus 分页对象
     */
    public <T> Page<T> toMpPageDefaultSortByCreateTimeDesc() {
        return toMpPageWithDefaultSort("create_time", false);
    }

    /**
     * 使用更新时间降序排序生成分页对象。
     *
     * @param <T> 数据类型
     * @return MyBatis-Plus 分页对象
     */
    public <T> Page<T> toMpPageDefaultSortByUpdateTimeDesc() {
        return toMpPageWithDefaultSort("update_time", false);
    }

    /**
     * 验证排序字段是否合法，防止 SQL 注入。
     *
     * @param sortBy 排序字段
     * @return 是否合法
     */
    private boolean isSortByValid(String sortBy) {
        List<String> validFields = Arrays.asList("create_time", "update_time");
        return validFields.contains(sortBy);
    }

    /**
     * 获取排序方式，如果未指定，默认为 false（降序）。
     *
     * @return 排序方式
     */
    private Boolean getIsAscOrDefault() {
        return isAsc != null ? isAsc : Boolean.FALSE;
    }

    // ================== 新增方法 ==================

    /**
     * 设置默认分页参数，如果页码或每页条数未指定则赋值默认值。
     */
    public void setDefaultsIfNull() {
        if (pageNo == null) {
            pageNo = 1L;
        }
        if (pageSize == null) {
            pageSize = 10L;
        }
    }


    /**
     * 验证分页参数是否合法。
     *
     * @return 是否合法
     */
    public boolean isValid() {
        return pageNo != null && pageSize != null && pageNo > 0 && pageSize > 0 && pageSize <= 100;
    }
}
