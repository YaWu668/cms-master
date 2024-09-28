package com.cms.common.security.aspect;

import com.cms.common.security.annotation.RequiresLogin;
import com.cms.common.security.annotation.RequiresPermissions;
import com.cms.common.security.annotation.RequiresRoles;
import com.cms.common.security.auth.AuthUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 基于 Spring Aop 的注解鉴权
 * 通过AOP实现对使用特定注解的方法进行权限检查
 * 本切面用于处理 @RequiresLogin、@RequiresPermissions、@RequiresRoles 注解
 *
 * @author 邓志军
 */
@Aspect
@Component
public class PreAuthorizeAspect {

    /**
     * 定义AOP签名 (切入所有使用鉴权注解的方法)
     * 使用@annotation表达式匹配所有使用RequiresLogin、RequiresPermissions、RequiresRoles注解的方法
     */
    public static final String POINTCUT_SIGN = " @annotation(com.cms.common.security.annotation.RequiresLogin) || "
            + "@annotation(com.cms.common.security.annotation.RequiresPermissions) || "
            + "@annotation(com.cms.common.security.annotation.RequiresRoles)";

    /**
     * 定义写入点
     */
    @Pointcut(POINTCUT_SIGN)
    public void pointcut() {
    }

    /**
     * 环绕切入
     * 对切入的方法进行环绕通知，实现权限检查
     *
     * @param joinPoint 切面对象
     * @return 底层方法执行后的返回值
     * @throws Throwable 底层方法抛出的异常
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法的签名，MethodSignature类能够获取目标方法的名称、返回类型、参数类型、注解和异常信息等。
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 检查方法上的注解
        this.checkMethodAnnotation(signature.getMethod());
        // 执行原有的逻辑
        return joinPoint.proceed();
    }

    /**
     * 对一个Method对象进行注解检查
     * 检查方法上的RequiresLogin、RequiresRoles、RequiresPermissions注解，并进行相应的权限验证
     */
    public void checkMethodAnnotation(Method method) {
        // 校验 @RequiresLogin 注解
        RequiresLogin requiresLogin = method.getAnnotation(RequiresLogin.class);

        // 校验 @RequiresRoles 注解
        RequiresRoles requiresRoles = method.getAnnotation(RequiresRoles.class);

        // 校验 @RequiresPermissions 注解
        RequiresPermissions requiresPermissions = method.getAnnotation(RequiresPermissions.class);
        if (requiresPermissions != null) {
            AuthUtil.checkPermi(requiresPermissions);
        }
    }


}
