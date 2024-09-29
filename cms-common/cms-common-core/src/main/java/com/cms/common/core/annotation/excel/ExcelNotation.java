package com.cms.common.core.annotation.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel导出批注
 *
 * @author 邓志军
 * @date 2024年9月5日10:50:14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelNotation {
    /**
     * 批注内容
     */
    String value() default "";

    /**
     * 批注行高, 一般不用设置
     */
    int remarkRowHigh() default 0;

    /**
     * 批注列宽, 根据导出情况调整
     */
    int remarkColumnWide() default 0;
}
