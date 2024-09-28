package com.cms.common.core.exception.auth;

import org.apache.commons.lang3.StringUtils;

/**
 * 未能通过的角色认证异常
 *
 * @author 邓志军
 * @date 2024年5月29日13:42:30
 */
public class NotRoleException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NotRoleException(String role) {
        super(role);
    }

    public NotRoleException(String[] roles) {
        super(StringUtils.join(roles, ","));
    }
}
