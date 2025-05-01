package com.xk.check.Validator;

import com.cms.common.core.exception.ServiceException;
import com.xk.check.annotations.XSSValidation;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

public class XSSValidator implements ConstraintValidator<XSSValidation, String> {
    private static final PolicyFactory POLICY = new HtmlPolicyBuilder()
            .allowElements("b", "i", "u", "s")
            .allowAttributes("class").onElements("b", "i", "u", "s")
            .toFactory();

    @Override
    public void initialize(XSSValidation constraintAnnotation) {
        // 初始化方法，可以留空
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            return true; // 空值由@NotBlank等其他注解处理
        }

        // 检查是否包含恶意脚本
        String sanitized = POLICY.sanitize(value);
        if(!value.equals(sanitized)){
            throw new ServiceException("请求参数包含危险字符");
        }

        return true; // 如果清理后的字符串与原字符串相同，则通过验证
    }
}