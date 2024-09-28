package com.cms.common.security.handler;

import com.cms.common.core.enums.HttpStatusResponse;
import com.cms.common.core.exception.InnerAuthException;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.core.exception.auth.NotPermissionException;
import com.cms.common.core.utils.StringUtils;
import com.cms.common.core.web.domain.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 全局异常处理器
 *
 * @author 邓志军
 * @date 2024年5月28日16:21:53
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理方法参数校验异常
     *
     * @param e 方法参数校验异常对象
     * @return 返回自定义的错误响应对象
     */
    // 该注解指定了要处理的异常类型，即方法参数校验异常。
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Response<String> validationException(MethodArgumentNotValidException e) {
        logger.error("出现了数据校验异常，异常类型为:{}.异常信息为:{}", e.getClass(), e.getMessage());

        // 从特定异常中拿到异常结果集
        BindingResult bindingResult = e.getBindingResult();

        // 从异常结果集中拿到字段校验异常信息
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        // 返回异常信息
        int code = HttpStatusResponse.REQUIRED_PARAMETER_MISSING.getCode();
        return Response.error(code, fieldErrors.get(fieldErrors.size() - 1).getDefaultMessage());
    }

    /**
     * 权限码异常
     */
    @ExceptionHandler(NotPermissionException.class)
    public Response<String> handleNotPermissionException(NotPermissionException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        logger.error("请求地址'{}',权限码校验失败'{}'", requestURI, e.getMessage());
        return Response.error(HttpStatus.FORBIDDEN.value(), "没有访问权限!");
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public Response<String> handleServiceException(ServiceException e, HttpServletRequest request) {
        logger.error(e.getMessage(), e);
        Integer code = e.getCode();
        return StringUtils.isNotNull(code) ? Response.error(code, e.getMessage()) : Response.error(e.getMessage());
    }

    /**
     * 内部认证异常
     */
    @ExceptionHandler(InnerAuthException.class)
    public Response<String> handleInnerAuthException(InnerAuthException e) {
        return Response.error(e.getMessage());
    }
}
