package com.xk.domain.vo.detail;

import lombok.experimental.Accessors;

/**
 * 项目进度
 */
@lombok.Data
@Accessors(chain = true)
public class Schedule {
    /**
     * 内容
     */
    private String content;
    /**
     * 名字
     */
    private String nickName;
    /**
     * 账号(工号)
     */
    private String userName;
}
