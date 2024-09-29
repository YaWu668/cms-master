package com.common.config.mybtisPlus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;


/**
 * 定义MybatisPlus相关的配置
 *
 * @author 邓志军
 * @date 2024-05-28
 */
@Configuration
public class MybatisPlusConfig{

    /**
     * 用户批量注册Mybatis的插件bean
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        // 1.创建一个拦截器,用户批量注册MP插件
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 2.创建一个分页拦截器对象，并指定数据库类型为 MySQL
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，-1不受限制(每页大小默认没有上限)
        paginationInterceptor.setMaxLimit(-1L);

        // 3.将 分页拦截器对象 添加到 MybatisPlus的拦截器 中
        interceptor.addInnerInterceptor(paginationInterceptor);
        // 4.返回 MybatisPlus拦截器 对象
        return interceptor;
    }
}
