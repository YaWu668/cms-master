package com.cms.common.sensitive.enmus;

import cn.hutool.core.util.DesensitizedUtil;

import java.util.function.Function;

/**
 * 脱敏类型枚举
 *
 * 这个枚举定义了不同的脱敏类型及其对应的脱敏处理方法。利用 Java 8 的 Function 接口来实现字符串的脱敏操作。
 *
 * @author 邓志军
 * @date 2024年9月5日12:45:42
 */
public enum DesensitizedType {
    /**
     * 姓名脱敏，第2位字符用星号替换
     * 例如：李四 -> 李*四
     */
    USERNAME(s -> s.replaceAll("(\\S)\\S(\\S*)", "$1*$2")),

    /**
     * 密码脱敏，全部字符用星号替换
     * 使用 Hutool 的工具方法来处理密码脱敏
     */
    PASSWORD(DesensitizedUtil::password),

    /**
     * 身份证脱敏，中间10位字符用星号替换
     * 例如：123456789012345678 -> 1234** **** **** 5678
     */
    ID_CARD(s -> s.replaceAll("(\\d{4})\\d{10}(\\d{4})", "$1** **** ****$2")),

    /**
     * 手机号脱敏，中间4位字符用星号替换
     * 例如：13812345678 -> 138****5678
     */
    PHONE(s -> s.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2")),

    /**
     * 电子邮箱脱敏，仅显示第一个字母和@后面的地址，其他字符用星号替换
     * 例如：example@example.com -> e****@example.com
     */
    EMAIL(s -> s.replaceAll("(^.)[^@]*(@.*$)", "$1****$2")),

    /**
     * 银行卡号脱敏，保留最后4位，其他字符用星号替换
     * 例如：1234567812345678 -> **** **** **** **** 5678
     */
    BANK_CARD(s -> s.replaceAll("\\d{15}(\\d{3})", "**** **** **** **** $1")),

    /**
     * 车牌号码脱敏，包含普通车辆和新能源车辆
     * 使用 Hutool 的工具方法来处理车牌号脱敏
     */
    CAR_LICENSE(DesensitizedUtil::carLicense);

    // 脱敏处理函数
    private final Function<String, String> desensitizer;

    /**
     * 构造函数，初始化脱敏处理函数
     *
     * @param desensitizer 脱敏处理函数
     */
    DesensitizedType(Function<String, String> desensitizer) {
        this.desensitizer = desensitizer;
    }

    /**
     * 获取脱敏处理函数
     *
     * @return 脱敏处理函数
     */
    public Function<String, String> desensitizer() {
        return desensitizer;
    }
}
