package com.cms.system.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 修改角色状态
 *
 * @author 邓志军
 * @date 2024年9月4日17:54:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateStatusVo {

    /**
     * 角色id
     */
    @NotNull(message = "缺少必要参数userId")
    private Long userId;

    /**
     * 帐号状态（0正常 1停用）
     */
    @ApiModelProperty(value = "帐号状态（0正常 1停用）", position = 2)
    @NotNull(message = "缺少必要参数 status")
    @Pattern(regexp = "0|1", message = "status出现意外的值!")
    private String status;
}
