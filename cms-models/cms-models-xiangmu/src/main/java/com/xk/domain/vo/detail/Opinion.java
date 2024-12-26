package com.xk.domain.vo.detail;

import lombok.experimental.Accessors;

/**
 * 审核意见
 */
@lombok.Data
@Accessors(chain = true)
public class Opinion {
    /**
     * 审核意见
     */
    private String auditOpinion;
    /**
     * 审核状态(0表示通过, 1表示不通过)
     */
    private Long auditState;
    /**
     * 名字
     */
    private String nickName;
    /**
     * 角色
     */
    private String role;
    /**
     * 账号(工号)
     */
    private String userName;
}
