package com.common.config.mybtisPlus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.cms.common.core.constant.SecurityConstants;
import com.cms.common.core.context.SecurityContextHolder;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * MyBatisPlus自动填充数据处理器
 *
 * @author 邓志军
 * @date 2024-05-29
 */
@Configuration
public class MyBatisPlusMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入数据时的自动填充方法
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 获取用户名
        String userName = SecurityContextHolder.getUserName();
        // 设置插入的时间为当前时间
        metaObject.setValue("createTime", new Date());
        // 设置添加数据人
        metaObject.setValue("createBy", userName);
    }

    /**
     * 更新数据时的自动填充方法
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 获取用户名
        String userName = SecurityContextHolder.getUserName();
        // 设置修改的时间为当前时间
        metaObject.setValue("updateTime", new Date());
        // 设置修改数据人
        metaObject.setValue("updateBy", userName);
    }
}
