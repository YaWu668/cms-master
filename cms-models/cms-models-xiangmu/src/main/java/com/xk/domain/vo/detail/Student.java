package com.xk.domain.vo.detail;

import lombok.experimental.Accessors;

/**
 * 学生信息
 */
@lombok.Data
@Accessors(chain = true)
public class Student {
    /**
     * 学院组的id
     */
    private Long collegeGroupId;
    /**
     * 出生年月
     */
    private String dateOfBirth;
    /**
     * 是否负责人 , 0为不是, 1是(只能存在一个)
     */
    private Integer isPrincipal;
    /**
     * 分工
     */
    private String job;
    /**
     * 邮箱
     */
    private String mailbox;
    /**
     * 手机
     */
    private String mobilePhone;
    /**
     * 名字
     */
    private String name;
    /**
     * 民族,字典(xm_item_nation) 0为汉族
     */
    private Long nation;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 专业班级,(用户自己输入)
     */
    private String professionalClass;
    /**
     * 性别 (字典:sys_user_sex)
     */
    private Long sex;
    /**
     * 学生
     */
    private Long userId;
    /**
     * 学号
     */
    private String userName;
}
