package com.cms.common.log.aspect;

import com.alibaba.fastjson2.JSON;
import com.cms.common.core.utils.ip.IpUtils;
import com.cms.common.core.utils.ServletUtils;
import com.cms.common.core.utils.StringUtils;
import com.cms.common.log.annotation.Log;
import com.cms.common.log.enums.BusinessStatus;
import com.cms.common.log.filter.PropertyPreExcludeFilter;
import com.cms.common.log.service.AsyncLogService;
import com.cms.common.security.utils.SecurityUtils;
import com.cms.system.api.domain.pojo.SysOperLog;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * 操作日志记录处理
 *
 * @author 邓志军
 * @date 2024年5月28日23:45:31
 */
@Aspect
@Component
public class LogAspect {

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private AsyncLogService asyncLogService;

    /**
     * 排除敏感属性字段
     */
    public static final String[] EXCLUDE_PROPERTIES = {"password", "oldPassword", "newPassword", "confirmPassword"};

    /**
     * 计算操作消耗时间
     */
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal<Long>("Cost Time");

    /**
     * 处理请求前执行
     */
    @Before(value = "@annotation(controllerLog)")
    public void boBefore(JoinPoint joinPoint, Log controllerLog) {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }

    /**
     * 处理日志记录
     *
     * @param joinPoint      切点信息
     * @param controllerLog  控制器日志注解
     * @param e              异常信息
     * @param jsonResult     JSON结果
     */
    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult) {
        try {
            // 创建操作日志对象
            SysOperLog operLog = new SysOperLog();
            // 设置操作状态为成功
            operLog.setStatus(BusinessStatus.SUCCESS.ordinal());

            // 设置操作时间
            operLog.setOperTime(new Date());

            // 获取请求的IP地址
            String ip = IpUtils.getIpAddr();
            operLog.setOperIp(ip);

            // 获取请求的URL
            operLog.setOperUrl(StringUtils.substring(Objects.requireNonNull(ServletUtils.getRequest()).getRequestURI(), 0, 255));

            // 获取当前用户名
            String username = SecurityUtils.getUsername();
            if (StringUtils.isNotBlank(username)) {
                operLog.setOperName(username);
            }

            // 如果存在异常，设置操作状态为失败，并记录异常信息
            if (e != null) {
                operLog.setStatus(BusinessStatus.FAIL.ordinal());
                operLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            }

            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName + "()");

            // 设置请求方式
            operLog.setRequestMethod(ServletUtils.getRequest().getMethod());

            // 处理注解上的参数
            getControllerMethodDescription(joinPoint, controllerLog, operLog, jsonResult);

            // 设置操作耗时
            operLog.setCostTime(System.currentTimeMillis() - TIME_THREADLOCAL.get());

            // 保存日志到数据库
            asyncLogService.saveSysLog(operLog);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        } finally {
            // 清除线程变量
            TIME_THREADLOCAL.remove();
        }
    }


    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log     日志
     * @param operLog 操作日志
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, SysOperLog operLog, Object jsonResult) throws Exception {
        // 设置action动作
        operLog.setBusinessType(log.businessType().ordinal());
        // 设置标题
        operLog.setTitle(log.title());
        // 设置操作人类别
        operLog.setOperatorType(log.operatorType().ordinal());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, operLog, log.excludeParamNames());
        }
        // 是否需要保存response，参数和值
        if (log.isSaveResponseData() && StringUtils.isNotNull(jsonResult)) {
            operLog.setJsonResult(StringUtils.substring(JSON.toJSONString(jsonResult), 0, 2000));
        }
    }

    /**
     * 将请求参数设置到操作日志中
     *
     * @param joinPoint         切点
     * @param operLog           操作日志对象
     * @param excludeParamNames 需要排除的参数名数组
     */
    private void setRequestValue(JoinPoint joinPoint, SysOperLog operLog, String[] excludeParamNames) throws Exception {
        // 获取请求方法类型和参数Map
        String requestMethod = operLog.getRequestMethod();
        Map<?, ?> paramsMap = ServletUtils.getParamMap(ServletUtils.getRequest());

        if (StringUtils.isEmpty(paramsMap) && (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod))) {
            // 如果参数Map为空且请求方法为PUT或POST，则将方法参数转换为字符串设置到操作日志中
            String params = argsArrayToString(joinPoint.getArgs(), excludeParamNames);
            operLog.setOperParam(StringUtils.substring(params, 0, 2000));
        } else {
            // 如果参数Map不为空，将参数Map转换为JSON字符串并设置到操作日志中
            operLog.setOperParam(StringUtils.substring(JSON.toJSONString(paramsMap, excludePropertyPreFilter(excludeParamNames)), 0, 2000));
        }
    }

    /**
     * 将参数数组拼装为字符串
     *
     * @param paramsArray       参数数组
     * @param excludeParamNames 需要排除的参数名数组
     * @return 拼装后的参数字符串
     */
    private String argsArrayToString(Object[] paramsArray, String[] excludeParamNames) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object param : paramsArray) {
                if (StringUtils.isNotNull(param) && !isFilterObject(param)) {
                    // 如果参数不为空且不是需要过滤的对象，则将其转换为JSON字符串并添加到参数字符串中
                    try {
                        String jsonObj = JSON.toJSONString(param, excludePropertyPreFilter(excludeParamNames));
                        params.append(jsonObj).append(" ");
                    } catch (Exception ignored) {
                        // 忽略转换异常
                    }
                }
            }
        }
        return params.toString().trim();
    }

    /**
     * 忽略敏感属性
     */
    public PropertyPreExcludeFilter excludePropertyPreFilter(String[] excludeParamNames) {
        return new PropertyPreExcludeFilter().addExcludes(ArrayUtils.addAll(EXCLUDE_PROPERTIES, excludeParamNames));
    }

    /**
     * 判断是否为需要过滤的对象。
     *
     * @param o 对象信息
     * @return 如果是需要过滤的对象，则返回true；否则返回false
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            // 如果是数组，判断数组元素类型是否是 MultipartFile
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            // 如果是集合，遍历集合中的元素，判断元素类型是否是 MultipartFile
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            // 如果是Map，遍历Map中的值，判断值的类型是否是 MultipartFile
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        // 判断是否为特定类型的对象，如 MultipartFile、HttpServletRequest、HttpServletResponse、BindingResult
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }
}
