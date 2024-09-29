package com.cms.common.security.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cms.common.core.constant.SecurityConstants;
import com.cms.common.core.context.SecurityContextHolder;
import com.cms.common.core.utils.ServletUtils;
import com.cms.common.core.utils.StringUtils;
import com.cms.common.security.auth.AuthUtil;
import com.cms.common.security.utils.SecurityUtils;
import com.cms.system.api.model.LoginUser;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

/**
 * 自定义请求头拦截器，将Header数据封装到线程变量中方便获取
 * 注意：此拦截器会同时验证当前用户有效期自动刷新有效期
 *
 * 该拦截器用于将请求头数据封装到线程变量中，以便在应用程序的其他地方轻松访问。同时，它还负责验证当前用户的有效期，并自动刷新有效期。
 *
 * @author 邓志军
 * @date 2024年5月29日13:42:30
 */
public class HeaderInterceptor implements AsyncHandlerInterceptor {

    /**
     * 在处理请求之前执行的方法，将请求头数据封装到线程变量中并验证用户有效期
     *
     * @param request   HttpServletRequest对象
     * @param response  HttpServletResponse对象
     * @param handler   被处理的处理程序对象
     * @return 如果继续处理，则返回true；否则返回false
     * @throws Exception 可能抛出的异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 将请求头数据封装到SecurityContextHolder中
        SecurityContextHolder.setUserId(ServletUtils.getHeader(request, SecurityConstants.DETAILS_USER_ID));
        SecurityContextHolder.setUserName(ServletUtils.getHeader(request, SecurityConstants.DETAILS_USERNAME));
        SecurityContextHolder.setUserKey(ServletUtils.getHeader(request, SecurityConstants.USER_KEY));

        // 获取token并验证用户有效期
        String token = SecurityUtils.getToken();
        if (StringUtils.isNotEmpty(token)) {
            LoginUser loginUser = AuthUtil.getLoginUser(token);
            if (StringUtils.isNotNull(loginUser)) {
                AuthUtil.verifyLoginUserExpire(loginUser);
                SecurityContextHolder.set(SecurityConstants.LOGIN_USER, loginUser);
            }
        }
        return true;
    }

    /**
     * 请求处理完成后执行的方法，清除线程变量
     *
     * @param request   HttpServletRequest对象
     * @param response  HttpServletResponse对象
     * @param handler   被处理的处理程序对象
     * @param ex        可能抛出的异常
     * @throws Exception 可能抛出的异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // 清除线程变量
        SecurityContextHolder.remove();
    }
}
