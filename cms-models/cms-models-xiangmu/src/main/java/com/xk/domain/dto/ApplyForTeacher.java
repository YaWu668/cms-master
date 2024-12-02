package com.xk.domain.dto;

@lombok.Data
public class ApplyForTeacher {
    /**
     * 是否指导老师(0代表是指导老师, 1代表是企业老师)
     */
    private Long isTeacher;
    /**
     * 邮箱
     */
    private String mailbox;
    /**
     * 名字
     */
    private String name;
    /**
     * 手机
     */
    private String phone;
    /**
     * 职位
     */
    private String post;
    /**
     * 单位
     */
    private String unit;
}
