package com.cms.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; // 添加此导入

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/**
 * 跨域响应头过滤器，用于处理响应头中的跨域相关设置
 * 此过滤器旨在确保跨域请求时，响应头中的相关字段得到正确处理
 */
@Component
public class CorsResponseHeaderFilter implements GlobalFilter, Ordered {

    // 日志记录器，用于记录日志信息
    private static final Logger logger = LoggerFactory.getLogger(CorsResponseHeaderFilter.class);

    // 表示任意来源
    private static final String ANY = "*";

    /**
     * 获取过滤器的执行顺序
     *
     * @return 返回过滤器的执行顺序，本例中设置在NettyWriteResponseFilter之后执行
     */
    @Override
    public int getOrder() {
        // 指定此过滤器位于NettyWriteResponseFilter之后
        // 即待处理完响应体后接着处理响应头
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER + 1;
    }

    /**
     * 过滤方法，处理跨域响应头
     *
     * @param exchange 服务器Web交换对象，包含请求和响应信息
     * @param chain 过滤链，用于执行下一个过滤器
     * @return 返回一个Mono<Void>，表示异步处理完成
     */
    @Override
    @SuppressWarnings("serial")
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 链式处理，先执行链中的下一个过滤器，然后处理响应头
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            // 处理响应头中的跨域设置
            exchange.getResponse().getHeaders().entrySet().stream()
                    .filter(kv -> (kv.getValue() != null && kv.getValue().size() > 1))
                    .filter(kv -> (kv.getKey().equals(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)
                            || kv.getKey().equals(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS)
                            || kv.getKey().equals(HttpHeaders.VARY)))
                    .forEach(kv ->
                    {
                        // 对Vary头进行去重处理
                        if(kv.getKey().equals(HttpHeaders.VARY))
                            kv.setValue(kv.getValue().stream().distinct().collect(Collectors.toList()));
                        else{
                            List<String> value = new ArrayList<>();
                            // 对跨域允许来源和凭证头进行处理
                            if(kv.getValue().contains(ANY)){  //如果包含*，则取*
                                value.add(ANY);
                                kv.setValue(value);
                            }else{
                                value.add(kv.getValue().get(0)); // 否则默认取第一个
                                kv.setValue(value);
                            }
                        }
                    });
        }));
    }
}
