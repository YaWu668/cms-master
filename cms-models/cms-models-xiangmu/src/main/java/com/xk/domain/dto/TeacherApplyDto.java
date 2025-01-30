package com.xk.domain.dto;

import lombok.Data;

/**
 * 添加教师时
 */
@Data
public class TeacherApplyDto {
    /**
     * 老师报名表的ID
     */
    private Long teacherApplyId;
    /**
     * 用户表的id
     */
    private Long userId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 单位
     */
    private String unit;
    /**
     * 职位
     */
    private String post;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 邮箱
     */
    private String mailbox;
    /**
     * 是否指导老师(0代表是指导老师, 1代表是企业老师)
     */
    private Integer isTeacher;
}
