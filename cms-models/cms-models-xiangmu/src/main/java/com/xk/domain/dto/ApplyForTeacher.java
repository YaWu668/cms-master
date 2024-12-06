package com.xk.domain.dto;

;
import com.cms.common.core.constant.VerifyConstants;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@lombok.Data
@lombok.ToString
@lombok.experimental.Accessors(chain = true)
public class ApplyForTeacher {
    /**
     * 是否指导老师(0代表是指导老师, 1代表是企业老师)
     */
    @NotNull(message = "请选择是否指导老师")
    private Long isTeacher;
    /**
     * 邮箱
     */
    @NotNull(message = "老师的邮箱不能为空")
    @Email(message = "老师的邮箱格式不正确")
    private String mailbox;
    /**
     * 名字
     */
    @NotNull(message = "姓名不能为空")
    private String name;
    /**
     * 手机
     */
    @NotNull(message = "老师的手机号不能为空")
    @Pattern(regexp = "^((13[0-9])|(14[5,7,9])|(15[^4,\\D])|(16[6])|(17[0-8])|(18[0-9])|(19[1,8,9]))\\d{8}", message = "老师的手机号格式不正确")
    private String phone;
    /**
     * 职位
     */
    @NotNull(message = "老师的职位不能为空")
    private String post;
    /**
     * 单位
     */
    @NotNull(message = "老师的单位不能为空")
    private String unit;
    /**
     * 老师的用户id
     */
    @NotNull(message = "老师的用户id不能为空")
    private Long userId;
}
