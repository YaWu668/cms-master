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
        Long userId = null;
        try {
            userId = SecurityUtils.getUserId();
        } catch (Exception e) {
            log.error("正常异常------这个异常是注册账号时候解析token失败,获取不到 use  rid 失败异常");
            e.printStackTrace();
            userId = -1L;//表示是自己创建
        }

        if (TaskContext.isTaskContext()) {
            // 定时任务的插入逻辑
            this.setFieldValByName("createTime", new Date(), metaObject);
            this.setFieldValByName("createBy",-1L , metaObject);
            this.setFieldValByName("updateTime", new Date(), metaObject);
            this.setFieldValByName("updateBy", -1L, metaObject);
            log.info("定时任务的插入逻辑");
        } else {
            // 正常插入逻辑
            this.setFieldValByName("createTime", new Date(), metaObject);
            this.setFieldValByName("createBy",userId , metaObject);
            this.setFieldValByName("updateTime", new Date(), metaObject);
            this.setFieldValByName("updateBy", userId, metaObject);
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Long userId = null;
        try {
            userId = SecurityUtils.getUserId();
        } catch (Exception e) {
            log.error("正常异常------这个异常是注册账号时候解析token失败,获取不到 use  rid 失败异常");
            e.printStackTrace();
            userId = -1L;//表示是自己创建
        }
        if (TaskContext.isTaskContext()) {
            // 定时任务的插入逻辑
            this.setFieldValByName("createTime", new Date(), metaObject);
            this.setFieldValByName("createBy",-1L , metaObject);
            this.setFieldValByName("updateTime", new Date(), metaObject);
            this.setFieldValByName("updateBy", -1L, metaObject);
            log.info("定时任务的插入逻辑");
        } else {
            // 正常插入逻辑
            this.setFieldValByName("createTime", new Date(), metaObject);
            this.setFieldValByName("createBy",userId , metaObject);
            this.setFieldValByName("updateTime", new Date(), metaObject);
            this.setFieldValByName("updateBy", userId, metaObject);
        }
    }
}
