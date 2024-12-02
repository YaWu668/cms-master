package com.xk.domain.dto;

@lombok.Data
public class ApplyForStudent {
    /**
     * 学院组的id
     */
    private Long college;
    /**
     * 出生年月
     */
    private String dateOfBirth;
    /**
     * 是否负责人 , 0为不是, 1是(只能存在一个)
     */
    private Long isPrincipal;
    /**
     * 项目分工
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
     * 字典
     */
    private String nation;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 专业班级,(用户自己输入)
     */
    private String professionalClass;
    /**
     * 性别
     */
    private Long sex;
    /**
     * 学号
     */
    private Long stuentNums;
}
