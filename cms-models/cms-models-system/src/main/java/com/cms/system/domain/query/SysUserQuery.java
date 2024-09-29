package com.cms.system.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统用户列表查询模型
 *
 * @author 邓志军
 * @date 2024年5月30日19:04:35
 */
@ApiModel(description = "系统用户列表查询模型")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUserQuery {

    /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户账号", position = 1)
    private String userName;

    /**
     * 用户状态
     */
    @ApiModelProperty(value = "用户状态", position = 2)
    private Integer status;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", position = 3)
    private String phonenumber;

    /**
     * 部门ID
     */
    @ApiModelProperty(value = "部门ID", position = 4)
    private Long deptId;
}
