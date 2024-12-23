package com.xk.domain.vo.student;

import lombok.Data;

/**
 * 添加学生时
 */
@Data
public class StudentApplyVo {
    //报名表id
    private Long studnetApplyId;
    //姓名
    private String name;
    //性别(字典)
    private Long sex;
    //学号
    private String userName;
    //民族(字典)
    private Long nation;
    //出生年月(格式:yyyy-rr-xxx)
    private String dateOfBirth;
    //学院(学院组的id)
    private Long collegeGroupId;
    //专业班级,(用户自己输入)
    private String professionalClass;
    //联系电话,(用户自己输入)
    private String phone;
    //手机(不知道搞什么的)
    private String mobilePhone;
    //邮箱(用户自己输入)
    private String mailbox;
    //项目中的分工(用户自己输入)
    private String job;
    //是否负责人 , 0为不是, 1是
    private Integer isPrincipal;
}
