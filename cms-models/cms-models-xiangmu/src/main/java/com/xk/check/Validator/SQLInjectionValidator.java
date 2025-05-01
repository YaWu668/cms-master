package com.xk.check.Validator;

import com.cms.common.core.exception.ServiceException;
import com.xk.check.annotations.SQLInjectionSafe;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class SQLInjectionValidator implements ConstraintValidator<SQLInjectionSafe, String> {
    // 定义常见的SQL注入危险模式
    private static final String[] SQL_INJECTION_PATTERNS = {
            "(?i)\\b(select|insert|update|delete|drop|alter|create|truncate|grant|revoke|union|exec)\\b",
            "(?i)\\b(from|into|where|having|join|on|group\\s+by|order\\s+by)\\b",
            "--", // SQL注释
            "/\\*.*?\\*/", // SQL多行注释
            ";", // 语句分隔符
            "\\b(or|and)\\s+['\"\\s]*[0-1]=[0-1]['\"\\s]*\\b", // 1=1 或类似模式
            "\\b(or|and)\\s+['\"\\s]*[a-zA-Z0-9]+=\\s*['\"\\s]*[a-zA-Z0-9]+['\"\\s]*\\b", // name='name' 模式
    };

    @Override
    public void initialize(SQLInjectionSafe constraintAnnotation) {
        // 初始化方法，可以留空
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            return true; // 空值由@NotBlank等其他注解处理
        }

        for (String pattern : SQL_INJECTION_PATTERNS) {
            if (value.matches(".*" + pattern + ".*")) {
                // 检测到SQL注入，抛出异常
                throw new ServiceException("非法餐数！！");
            }
        }

        return true;
    }
}