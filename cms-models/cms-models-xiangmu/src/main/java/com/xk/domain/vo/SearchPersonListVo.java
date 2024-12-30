package com.xk.domain.vo;

@lombok.Data
public class SearchPersonListVo {
    /**
     * 名字
     */
    private String nickName;
    /**
     * 用户的id
     */
    private Long userId;
    /**
     * 学号
     */
    private String userName;

    /**
     *   用户性别（0男 1女 2未知）
     */
    private String sex;
}
