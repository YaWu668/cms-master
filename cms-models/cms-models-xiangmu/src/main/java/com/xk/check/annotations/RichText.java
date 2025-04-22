package com.xk.check.annotations;



import com.xk.check.Validator.RichTextValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RichTextValidator.class)
public @interface RichText {
    String message() default "内容必须是富文本,并且文本格式为富文本,或者存文本内容";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
