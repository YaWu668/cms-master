package com.cms.common.core.enums;

/**
 * http响应统一数据返回
 *
 * @author 邓志军
 * @date 2024-05-28
 */
public enum HttpStatusResponse {
    SUCCESS(200, "操作成功"),
    REQUIRED_PARAMETER_MISSING(400, "缺少必要参数:"),
    CHECK_CODE_NOT_EMPTY(400, "验证码不能为空"),
    CHECK_CODE_ERROR(400, "验证码错误"),
    CHECK_CODE_TIMEOUT(400, "验证码已过期，请刷新重试"),
    USERNAME_OR_PASSWORD_ERROR(400, "用户名或密码错误"),
    PASSWORD_ERROR(400, "密码错误"),
    INVALID_TOKEN(401, "无效的Token"),
    TOKEN_ERROR(401, "用户未登录，请先登录"),
    NOT_PERMISSION(403, "没有权限访问该资源"),
    NOT_FOUND(404, "资源未找到"),
    ACCOUNT_EXISTS_ERROR(409, "该账号已存在"),
    SYSTEM_INTERNAL_EXCEPTION(500, "系统内部异常，请联系管理员"),
    ERROR(500, "系统内部异常，请联系管理员"),

    // 邮件相关的
    EMAIL_EXCEPTION(10012,"邮箱发送失败"),
    BAD_EMAILCODE_VERIFY(10007,"验证码无效或输入错误"),
    BINDING_EMAIL_EXCEPTION(10013,"邮箱绑定失败");

    private final int code;
    private final String msg;

    HttpStatusResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
