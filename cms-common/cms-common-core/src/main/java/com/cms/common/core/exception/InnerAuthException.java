package com.cms.common.core.exception;

/**
 * 内部认证异常
 *
 * @author 邓志军
 * @date 2024年5月28日22:30:29
 */
public class InnerAuthException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InnerAuthException(String message) {
        super(message);
    }
}
