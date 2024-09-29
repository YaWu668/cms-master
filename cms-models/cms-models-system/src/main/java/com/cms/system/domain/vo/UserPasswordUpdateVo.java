package com.cms.system.domain.vo;

import com.cms.common.core.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 修改密码
 *
 * @author 邓志军
 * @date 2024年9月4日08:37:15
 */
@ApiModel(description = "修改密码")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPasswordUpdateVo {
    /**
     * 密码
     */
    @NotNull(message = "缺少必要参数password")
    @ApiModelProperty(value = "密码", position = 1)
    private String password;

    /**
     * 确认密码
     */
    @NotNull(message = "缺少必要参数checkPassword")
    @ApiModelProperty(value = "确认密码", position = 2)
    private String checkPassword;

    /**
     * 确认密码
     *
     * @return 一致返回 true
     */
    public boolean confirmPassword() {
        return StringUtils.isNotEmpty(this.password)
                && StringUtils.isNotEmpty(this.checkPassword)
                && this.password.equals(this.checkPassword);
    }
}
