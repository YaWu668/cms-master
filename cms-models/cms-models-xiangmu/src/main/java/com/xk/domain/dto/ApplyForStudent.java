package com.xk.domain.dto;

import com.cms.common.core.constant.VerifyConstants;

import javax.validation.constraints.*;

@lombok.Data
public class ApplyForStudent {
    /**
     * 学院组的id
     */
    @NotNull(message = "学生的学院组的id不能为空")
    private Long collegeGroupId;
    /**
     * 出生年月
     */
    @NotNull(message = "出生年月不能为空")
    @Pattern(regexp ="^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$" , message = "出生日期格式不正确!!!, 正确格式为:yyyy-MM-dd")
    @NotEmpty(message = "出生年月不能为空")
    private String dateOfBirth;
    /**
     * 是否负责人 , 0为不是, 1是(只能存在一个)
     */
    @NotNull(message = "是否负责人不能为空")
    @Min(value = 0, message = "isPrincipal是否负责人不能小于0")
    @Max(value = 1, message = "isPrincipal是否负责人不能大于1")
    private Long isPrincipal;
    /**
     * 项目分工
     */
    @NotNull(message = "项目分工不能为空")
    @NotEmpty(message = "项目分工不能为空")
    private String job;
    /**
     * 邮箱
     */
    @NotNull(message = "学生的邮箱不能为空")
    @Email(message = "学生的邮箱格式不正确!!! ")
    @NotEmpty(message = "学生的邮箱不能为空")
    private String mailbox;
    /**
     * 手机
     */
    private String mobilePhone;
    /**
     * 名字
     */
    @NotNull(message = "名字不能为空")
    @NotEmpty(message = "名字不能为空")
    private String name;
    /**
     * 民族,字典 0为汉族,从0开始到55结束
     */
    @NotNull(message = "民族不能为空")
    @Min(value = 0, message = "民族不能小于0")
    @Max(value = 55, message = "民族不能大于55")
    private Long nation;
    /**
     * 联系电话
     */
    @NotNull(message = "联系电话不能为空")
    @Pattern(regexp = "^((13[0-9])|(14[5,7,9])|(15[^4,\\D])|(16[6])|(17[0-8])|(18[0-9])|(19[1,8,9]))\\d{8}", message = "联系电话格式不正确!!! ")
    private String phone;
    /**
     * 专业班级,(用户自己输入)
     */
    @NotEmpty(message = "专业班级不能为空")
    @Pattern(regexp = "^([1-9]\\d)级([\\u4e00-\\u9fa5]{2,})(0[1-9]|[1-9]\\d)班$\n", message = "专业班级格式不正确!!! 正确格式:21级计算机科学与技术03班")
    private String professionalClass;
    /**
     * 性别
     */
    @NotNull(message = "性别不能为空")
    @Min(value = 0, message = "性别不能小于0")
    @Max(value = 1, message = "性别不能大于1")
    private Long sex;
    /**
     * 学号
     */
    @NotNull(message = "学号不能为空")
    @NotEmpty(message = "学号不能为空")
    private  String userName;
    /**
     * 学生的用户id
     */
    @NotNull(message = "学生的用户id不能为空")
    private Long userId;
}
