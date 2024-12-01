package com.cms.auth.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录对象
 *
 * @author 邓志军
 * @date 2024年5月28日21:51:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginVo {
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;


    /**
     * 用户密码
     */
    @NotBlank(message = "用户密码不能为空")

    private String password;
}
