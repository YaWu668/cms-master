package com.xk.domain.vo.student;

import lombok.Data;

/**
 * 添加学生时
 */
@Data
public class StudentVo {
    /**
     * 学生的id
     */
    private Long userId;

    /**
     * 学生的学号
     */
    private String userName;
    /**
     * 学生的名字
     */
    private String nickName;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 学院名称
     */
    private String schoolName;
    /**
     * 专业
     */
    private String majorName;
}
