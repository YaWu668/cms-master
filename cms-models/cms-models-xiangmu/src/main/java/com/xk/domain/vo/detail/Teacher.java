package com.xk.domain.vo.detail;

import lombok.experimental.Accessors;
/**
 * 教师信息
 */
@lombok.Data
@Accessors(chain = true)
public class Teacher {
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
    /**
     * 用户id
     */
    private Long userId;
}
