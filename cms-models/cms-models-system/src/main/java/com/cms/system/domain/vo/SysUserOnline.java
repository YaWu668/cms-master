package com.cms.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 当前在线会话
 *
 * @author 邓志军
 * @date 2024年9月5日14:55:22
 */
@ApiModel(description = "当前在线会话")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysUserOnline {
    /**
     * 会话编号
     */
    @ApiModelProperty(value = "会话编号", position = 1)
    private String tokenId;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称", position = 2)
    private String userName;

    /**
     * 登录IP地址
     */
    @ApiModelProperty(value = "登录IP地址", position = 3)
    private String ipaddr;

    /**
     * 登录时间
     */
    @ApiModelProperty(value = "登录时间", position = 4)
    private Long loginTime;
}
