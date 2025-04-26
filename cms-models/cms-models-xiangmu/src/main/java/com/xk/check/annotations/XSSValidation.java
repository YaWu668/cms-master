package com.xk.check.annotations;

import com.xk.check.Validator.XSSValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = XSSValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface XSSValidation {
    String message() default "请求包含非法参数！！！";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}