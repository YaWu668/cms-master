package com.cms.common.core.enums;

/**
 * 用户状态
 *
 * @author 邓志军
 * @date 2024年5月28日22:55:51
 */
public enum UserStatus {
    OK("0", "正常"), DISABLE("1", "停用"), DELETED("1", "删除");

    private final String code;
    private final String info;

    UserStatus(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
