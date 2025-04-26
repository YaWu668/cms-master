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
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            return true;
        }
        String sanitized = POLICY.sanitize(value);
        if (!value.equals(sanitized)) {
            throw new ServiceException("非法请求!!输入包含潜在的XSS攻击内容!");
        }
        return true;
    }
}