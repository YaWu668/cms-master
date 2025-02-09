package com.xk.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.cms.common.security.utils.SecurityUtils;
import com.xk.utils.TaskContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 自定义填充公共字段
 */
@Component
@Primary // 优先加载这个 Bean
@Slf4j
public class CustomMetaObjectHandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {
        String userName = null;
        try {
            userName = SecurityUtils.getUsername();
        } catch (Exception e) {
            userName = "定时任务执行";//表示是自己创建
        }

        if (TaskContext.isTaskContext()) {
            // 定时任务的插入逻辑
            this.setFieldValByName("createTime", new Date(), metaObject);
            this.setFieldValByName("createBy",userName , metaObject);
            this.setFieldValByName("updateTime", new Date(), metaObject);
            this.setFieldValByName("updateBy", userName, metaObject);
            log.info("定时任务的插入逻辑");
        } else {
            // 正常插入逻辑
            this.setFieldValByName("createTime", new Date(), metaObject);
            this.setFieldValByName("createBy",userName , metaObject);
            this.setFieldValByName("updateTime", new Date(), metaObject);
            this.setFieldValByName("updateBy", userName, metaObject);
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        String userName = null;
        try {
            userName = SecurityUtils.getUsername();
        } catch (Exception e) {
            userName = "定时任务执行";//表示是自己创建
        }

        if (TaskContext.isTaskContext()) {
            // 定时任务的插入逻辑
            this.setFieldValByName("updateTime", new Date(), metaObject);
            this.setFieldValByName("updateBy", userName, metaObject);
            log.info("定时任务的插入逻辑");
        } else {
            // 正常插入逻辑
            this.setFieldValByName("updateTime", new Date(), metaObject);
            this.setFieldValByName("updateBy", userName, metaObject);
        }
    }
}
