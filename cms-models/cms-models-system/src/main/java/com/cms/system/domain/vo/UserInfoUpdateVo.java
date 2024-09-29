package com.cms.system.domain.vo;

import com.cms.common.core.validation.ValidationGroups;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 用户信息修改
 *
 * @author 邓志军
 * @date 2024年8月30日08:58:54
 */
@ApiModel(description = "用户信息修改")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoUpdateVo {

    /**
     * 用户姓名
     */
    @NotNull(message = "缺少必要参数nickName")
    @ApiModelProperty(value = "用户姓名", position = 1)
    private String nickName;

    /**
     * 手机号码
     */
    @NotNull(message = "缺少必要参数phonenumber")
    @ApiModelProperty(value = "手机号码", position = 2)
    private String phonenumber;

    /**
     * 用户邮箱
     */
    @NotNull(message = "缺少必要参数email")
    @ApiModelProperty(value = "用户邮箱", position = 3)
    private String email;

    /**
     * 用户性别（0男 1女 2未知）
     */
    @ApiModelProperty(value = "用户性别（0男 1女 2未知）", position = 4)
    private String sex;

    /**
     * 微信号
     */
    @ApiModelProperty(value = "微信号", position = 5)
    private String weChatId;

    /**
     * QQ号
     */
    @ApiModelProperty(value = "QQ号", position = 6)
    private String qqId;

    /**
     * 婚姻状况(0未婚 1已婚 2离异 3丧偶)
     */
    @ApiModelProperty(value = "婚姻状况(0未婚 1已婚 2离异 3丧偶)", position = 7)
    private Integer maritalStatus;

    /**
     * 户口类型（0农村 1城市 2流动）
     */
    @ApiModelProperty(value = "户口类型（0农村 1城市 2流动）", position = 8)
    private Integer householdType;

    /**
     * 生育状况(0已育 1未育 2已结扎 3已绝育 4不孕)
     */
    @ApiModelProperty(value = "生育状况(0已育 1未育 2已结扎 3已绝育 4不孕)", position = 9)
    private Integer fertilityStatus;

    /**
     * 联系地址
     */
    @ApiModelProperty(value = "联系地址", position = 10)
    private String address;
}
