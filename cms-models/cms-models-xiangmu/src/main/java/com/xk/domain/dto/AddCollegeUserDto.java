package com.xk.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 学院添加人员
 */
@Data
@Accessors(chain = true)
public class AddCollegeUserDto {
    /**
     * 学院id
     */
    @NotNull(message = "学院id不能为空")
    @Min(value = 1, message = "学院id不能小于1")
    private Long collegeGroupId;
    /**
     * 用户的id
     */
    @NotNull(message = "用户id不能为空")
    @Min(value = 1, message = "用户id不能小于1")
    private Long userId;
    /**
     * 学院人员手机号
     */
    @NotNull(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phonenumber;
}
