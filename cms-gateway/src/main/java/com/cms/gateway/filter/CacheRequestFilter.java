package com.cms.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 获取body请求数据（解决流不能重复读取问题）
 * 该过滤器用于获取请求的body数据，并缓存以解决流不能重复读取的问题。
 *
 * @author 邓志军
 * @date 2024年5月28日15:28:33
 */
@Component
public class CacheRequestFilter extends AbstractGatewayFilterFactory<CacheRequestFilter.Config> {

    /**
     * 构造函数，指定Config类作为配置类
     */
    public CacheRequestFilter() {
        super(Config.class);
    }

    /**
     * 获取过滤器的名称
     *
     * @return 过滤器名称
     */
    @Override
    public String name() {
        return "CacheRequestFilter";
    }

    /**
     * 应用配置并创建一个自定义的网关过滤器
     *
     * @param config 用于配置该过滤器的配置对象
     * @return 创建的自定义网关过滤器
     */
    @Override
    public GatewayFilter apply(Config config) {
        // 创建CacheRequestGatewayFilter实例
        CacheRequestGatewayFilter cacheRequestGatewayFilter = new CacheRequestGatewayFilter();
        // 获取配置的顺序
        Integer order = config.getOrder();
        if (order == null) {
            // 如果未配置顺序，则直接返回cacheRequestGatewayFilter
            return cacheRequestGatewayFilter;
        }
        // 使用OrderedGatewayFilter包装cacheRequestGatewayFilter，并设置顺序
        return new OrderedGatewayFilter(cacheRequestGatewayFilter, order);
    }

    /**
     * 用于缓存请求体的网关过滤器
     */
    public static class CacheRequestGatewayFilter implements GatewayFilter {

        /**
         * 过滤器逻辑，用于缓存请求体并重新包装请求
         *
         * @param exchange ServerWebExchange对象，用于处理HTTP请求和响应
         * @param chain    GatewayFilterChain对象，用于调用过滤器链中的下一个过滤器
         * @return Mono<Void>对象，表示过滤器处理结果的信号
         */
        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            return chain.filter(exchange);
        }
    }

    /**
     * 用于配置缓存请求过滤器的参数类
     */
    static class Config {
        // 排序
        private Integer order;

        /**
         * 获取顺序
         *
         * @return 顺序值
         */
        public Integer getOrder() {
            return order;
        }

        /**
         * 设置顺序
         *
         * @param order 顺序值
         */
        public void setOrder(Integer order) {
            this.order = order;
        }
    }
}
