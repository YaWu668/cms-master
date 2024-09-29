package com.cms.gateway.filter;

import com.cms.common.core.constant.CacheConstants;
import com.cms.common.core.constant.SecurityConstants;
import com.cms.common.core.constant.TokenConstants;
import com.cms.common.core.utils.JwtUtils;
import com.cms.common.core.utils.ServletUtils;
import com.cms.common.core.utils.StringUtils;
import com.cms.common.redis.service.RedisService;
import com.cms.gateway.properties.IgnoreWhiteProperties;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 自定义的认证过滤器，用于网关鉴权
 *
 * @author 邓志军
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    // 注入用于配置需要跳过验证的URI地址的属性类
    @Autowired
    private IgnoreWhiteProperties ignoreWhite;

    // 注入RedisService用于操作缓存
    @Autowired
    private RedisService redisService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 获取原始的ServerHttpRequest对象，用于获取请求信息
        ServerHttpRequest request = exchange.getRequest();
        // 创建一个新的ServerHttpRequest.Builder对象，用于对原始请求对象进行修改
        ServerHttpRequest.Builder mutate = request.mutate();

        // 获取本地请求的路径
        String url = request.getURI().getPath();

        // 跳过不需要验证的路径，ignoreWhite.getWhites() 的配置存放与 nacos 配置中心中
        if (StringUtils.matches(url, ignoreWhite.getWhites())) {
            return chain.filter(exchange);
        }

        // 从请求头中获取 Token 信息
        String token = this.getToken(request);
        if (StringUtils.isEmpty(token)) {
            return this.unauthorizedResponse(exchange, "令牌不能为空");
        }

        // 解析并验证 Token
        Claims claims = JwtUtils.parseToken(token);
        if (claims == null) {
            return this.unauthorizedResponse(exchange, "令牌已过期或验证不正确！");
        }

        // 验证用户登录状态
        String userkey = JwtUtils.getUserKey(claims);
        boolean islogin = redisService.hasKey(getTokenKey(userkey));
        if (!islogin) {
            return this.unauthorizedResponse(exchange, "登录状态已过期");
        }

        // 从 Token 中获取用户信息
        String userid = JwtUtils.getUserId(claims);
        String username = JwtUtils.getUserName(claims);
        if (StringUtils.isEmpty(userid) || StringUtils.isEmpty(username)) {
            return unauthorizedResponse(exchange, "令牌验证失败");
        }

        // 设置用户信息到请求头中
        this.addHeader(mutate, SecurityConstants.USER_KEY, userkey);
        this.addHeader(mutate, SecurityConstants.DETAILS_USER_ID, userid);
        this.addHeader(mutate, SecurityConstants.DETAILS_USERNAME, username);
        // 清除内部请求来源参数
        this.removeHeader(mutate, SecurityConstants.FROM_SOURCE);

        // 继续请求处理链
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    /**
     * 移除请求头信息
     */
    private void removeHeader(ServerHttpRequest.Builder mutate, String name) {
        mutate.headers(httpHeaders -> httpHeaders.remove(name)).build();
    }

    /**
     * 添加请求头信息
     */
    private void addHeader(ServerHttpRequest.Builder mutate, String name, Object value) {
        if (value == null) {
            return;
        }
        String valueStr = value.toString();
        String valueEncode = ServletUtils.urlEncode(valueStr);
        mutate.header(name, valueEncode);
    }

    /**
     * 获取请求中的token
     */
    private String getToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(TokenConstants.AUTHENTICATION);
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX)) {
            token = token.replaceFirst(TokenConstants.PREFIX, StringUtils.EMPTY);
        }
        return token;
    }

    /**
     * 获取缓存key
     */
    private String getTokenKey(String token) {
        return CacheConstants.LOGIN_TOKEN_KEY + token;
    }

    /**
     * 返回过滤器的顺序，数值越小优先级越高
     */
    @Override
    public int getOrder() {
        return -200;
    }

    /**
     * 返回未授权的响应
     */
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String msg) {
        log.error("[鉴权异常处理]请求路径: {}", exchange.getRequest().getPath());
        return ServletUtils.webFluxResponseWriter(exchange.getResponse(), msg, HttpStatus.UNAUTHORIZED.value());
    }
}
