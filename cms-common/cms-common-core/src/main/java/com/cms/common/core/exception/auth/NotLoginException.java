package com.cms.common.core.exception.auth;

/**
 * 未能通过的登录认证异常
 *
 * @author 邓志军
 * @date 2024年5月29日13:42:30
 */
public class NotLoginException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NotLoginException(String message) {
        super(message);
    }
}
