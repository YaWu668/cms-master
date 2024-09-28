package com.cms.common.core.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cms.common.core.enums.HttpStatusResponse;
import com.cms.common.core.utils.DateUtils;
import com.cms.common.core.utils.PageUtils;
import com.cms.common.core.web.page.TableDataInfo;
import com.cms.common.core.web.domain.Response;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

/**
 * Web 层通用数据处理
 *
 * @author 邓志军
 * @date 2024年5月29日14:01:59
 */
public class BaseController {
    /**
     * 将前端传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据，自动装配上分页数据
     */
    protected void startPage() {
        PageUtils.startPage();
    }

    /**
     * 转换查询参数的开始和结束时间
     *
     * @param obj 需要转换的对象
     */
    protected void modifyObjectDateFields(Object obj) {

        String BEGIN_TIME = "beginTime";
        String END_TIME = "endTime";

        try {
            // 获取 Class 对象
            Class<?> clazz = obj.getClass();

            // 使用反射获取 beginTime 和 endTime 字段
            Field beginTimeField = clazz.getDeclaredField(BEGIN_TIME);
            Field endTimeField = clazz.getDeclaredField(END_TIME);

            // 设置可访问，否则会抛出 IllegalAccessException 异常
            beginTimeField.setAccessible(true);
            endTimeField.setAccessible(true);

            // 获取原始值
            Date beginTime = (Date) beginTimeField.get(obj);
            Date endTime = (Date) endTimeField.get(obj);

            // 更新修改后的值
            beginTimeField.set(obj, DateUtils.formatIntradayMinDateTime(beginTime));
            endTimeField.set(obj, DateUtils.formatIntradayMaxDateTime(endTime));
        } catch (IllegalAccessException | NoSuchFieldException ignored) {

        }
    }

    /**
     * 清理分页的线程变量
     */
    protected void clearPage() {
        PageUtils.clearPage();
    }

    /**
     * 将IPage转换为Response<TableDataInfo>
     *
     * @param page 包含分页数据的IPage对象
     * @return 包含分页数据的Response对象
     */
    public <T> Response<TableDataInfo<T>> transitionPageList(IPage<T> page) {
        TableDataInfo<T> pageInfo = new TableDataInfo<>();
        // 设置总页数
        pageInfo.setPages((int) page.getPages());
        // 设置总记录数
        pageInfo.setTotal(page.getTotal());
        // 设置当前页码
        pageInfo.setCurrent((int) page.getCurrent());
        // 设置每页记录数
        pageInfo.setSize((int) page.getSize());
        // 设置当前页数据列表
        pageInfo.setRecords(page.getRecords());
        // 返回成功响应对象，并携带分页信息
        return Response.success(pageInfo);
    }

    /**
     * 响应请求分页数据
     *
     * @param list 包含分页数据的列表
     * @param <T>  分页数据类型
     * @return 包含分页信息的响应对象
     */
    protected <T> Response<TableDataInfo<T>> getDataTable(List<T> list) {
        TableDataInfo<T> table = new TableDataInfo<>();
        // 使用PageInfo对列表进行分页信息的封装
        PageInfo<T> tableInfo = new PageInfo<>(list);
        // 设置总记录数
        table.setTotal(tableInfo.getTotal());
        // 设置当前页数据列表
        table.setRecords(list);
        // 设置当前页码
        table.setCurrent(tableInfo.getPageNum());
        // 设置总页数
        table.setPages(tableInfo.getPages());
        // 设置分页大小
        table.setSize(tableInfo.getSize());

        // 清除分页线程
        this.clearPage();

        // 返回成功响应对象，并携带分页信息
        return Response.success(table);
    }

    /**
     * 返回成功
     */
    public <T> Response<T> success() {
        return Response.success();
    }

    /**
     * 返回成功
     */
    public Response<String> success(String message) {
        return Response.success(message);
    }

    /**
     * 返回成功消息
     */
    public <T> Response<T> success(T data) {
        return Response.success(data);
    }

    /**
     * 返回失败
     */
    public Response<?> error() {
        return Response.error();
    }

    public Response<?> error(HttpStatusResponse h) {
        return Response.error();
    }

    /**
     * 返回失败消息
     */
    public Response<?> error(String message) {
        return Response.error(message);
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected Response<?> respond(int rows) {
        return rows > 0 ? Response.success() : Response.error();
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected Response<?> respond(boolean result) {
        return result ? success() : error();
    }
}
