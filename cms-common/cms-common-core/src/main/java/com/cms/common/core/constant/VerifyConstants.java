package com.cms.common.core.constant;

/**
 * 正则校验常量
 *
 * @author 邓志军
 * @date 2024年6月8日11:36:56
 */
public class VerifyConstants {
    /**
     * 手机号校验
     */
    public static final String PHONE_NUMBER_REGEX = "^((13[0-9])|(14[5,7,9])|(15[^4,\\D])|(16[6])|(17[0-8])|(18[0-9])|(19[1,8,9]))\\d{8}$";

    /**
     * 邮箱号校验
     */
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    /**
     * 车牌号校验
     */
    public static final String LICENSE_PLATE_NUMBER_REGEX = "^([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}(([0-9]{5}[DF])|([DF]([A-HJ-NP-Z0-9])[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳]{1})$";

    /**
     * 身份证号码验证正则表达式（仅适用于中国大陆18位身份证）
     */
    public static final String CHINA_ID_CARD_REGEX = "^[1-9]\\d{5}(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}(\\d|X|x)$";
}
