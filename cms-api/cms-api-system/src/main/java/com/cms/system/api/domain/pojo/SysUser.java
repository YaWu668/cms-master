package com.cms.system.api.domain.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cms.common.core.annotation.excel.ExcelNotation;
import com.cms.common.core.validation.ValidationGroups;
import com.cms.common.core.web.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息表
 *
 * @author 邓志军
 * @date 2024-05-29
 */
@ApiModel(description = "用户信息表")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUser extends BaseEntity implements Serializable {
    /**
     * 用户ID
     */
    @ColumnWidth(16)
    @ExcelProperty(value = "用户id", index = 0)
    @ApiModelProperty(value = "用户ID", position = 1)
    @NotNull(message = "缺少必要参数userId", groups = {ValidationGroups.Update.class})
    @Null(message = "出现意外参数userId", groups = {ValidationGroups.Insert.class})
    @TableId(type = IdType.AUTO)
    private Long userId;

    /**
     * 部门ID
     */
    @ColumnWidth(16)
    @ExcelProperty(value = "部门ID", index = 1)
    @ApiModelProperty(value = "部门ID", position = 2)
    private Long deptId;

    /**
     * 用户账号
     */
    @NotNull(message = "缺少必要参数userName", groups = {ValidationGroups.Insert.class})
    @ExcelProperty(value = "用户账号", index = 2)
    @ApiModelProperty(value = "用户账号", position = 3)
    private String userName;

    /**
     * 用户姓名
     */
    @NotNull(message = "缺少必要参数nickName", groups = {ValidationGroups.Insert.class})
    @ExcelProperty(value = "用户姓名", index = 3)
    @ApiModelProperty(value = "用户姓名", position = 4)
    private String nickName;

    /**
     * 用户类型（00系统用户）
     */
    @ExcelProperty(value = "用户类型", index = 4)
    @ExcelNotation("00系统用户")
    @ApiModelProperty(value = "用户类型（00系统用户）", position = 5)
    private String userType;

    /**
     * 用户邮箱
     */
    @NotNull(message = "缺少必要参数email", groups = {ValidationGroups.Insert.class})
    @ExcelProperty(value = "用户邮箱", index = 5)
    @ApiModelProperty(value = "用户邮箱", position = 6)
    private String email;

    /**
     * 手机号码
     */
    @NotNull(message = "缺少必要参数phonenumber", groups = {ValidationGroups.Insert.class})
    @ExcelProperty(value = "手机号码", index = 6)
    @ApiModelProperty(value = "手机号码", position = 7)
    private String phonenumber;

    /**
     * 用户性别（0男 1女 2未知）
     */
    @ExcelProperty(value = "用户性别", index = 7)
    @ExcelNotation("0男 1女 2未知")
    @ApiModelProperty(value = "用户性别（0男 1女 2未知）", position = 8)
    private String sex;

    /**
     * 头像地址
     */
    @ExcelProperty(value = "头像地址", index = 8)
    @ApiModelProperty(value = "头像地址", position = 9)
    private String avatar;


    /**
     * 密码
     */
    @ExcelIgnore
    @ApiModelProperty(value = "密码", position = 10)
    @NotNull(message = "缺少必要参数password", groups = {ValidationGroups.Insert.class})
    private String password;

    /**
     * 入职日期
     */
    @ColumnWidth(16)
    @ExcelProperty(value = "入职日期", index = 9)
    @ApiModelProperty(value = "入职日期", position = 11)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfHire;

    /**
     * 上级领导
     */
    @ExcelProperty(value = "上级领导", index = 10)
    @ApiModelProperty(value = "上级领导", position = 12)
    private Long leader;

    /**
     * 人员类型(0合同工 1派遣工 2实习生 3临时工)
     */
    @ExcelNotation("0合同工 1派遣工 2实习生 3临时工")
    @ExcelProperty(value = "人员类型", index = 11)
    @ApiModelProperty(value = "人员类型(0合同工 1派遣工 2实习生 3临时工)", position = 13)
    private Long personnelType;

    /**
     * 身份证号
     */
    @NotNull(message = "缺少必要参数idNumber", groups = {ValidationGroups.Insert.class})
    @ExcelProperty(value = "身份证号", index = 12)
    @ApiModelProperty(value = "身份证号", position = 14)
    private String idNumber;

    /**
     * 微信号
     */
    @ExcelProperty(value = "微信号", index = 13)
    @ApiModelProperty(value = "微信号", position = 15)
    private String weChatId;

    /**
     * QQ号
     */
    @ExcelProperty(value = "QQ号", index = 14)
    @ApiModelProperty(value = "QQ号", position = 16)
    private String qqId;

    /**
     * 出生日期
     */
    @ColumnWidth(16)
    @ExcelProperty(value = "出生日期", index = 15)
    @ApiModelProperty(value = "出生日期", position = 17)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    /**
     * 婚姻状况(0未婚 1已婚 2离异 3丧偶)
     */
    @ExcelNotation("0未婚 1已婚 2离异 3丧偶")
    @ExcelProperty(value = "婚姻状况", index = 16)
    @ApiModelProperty(value = "婚姻状况(0未婚 1已婚 2离异 3丧偶)", position = 18)
    private Integer maritalStatus;

    /**
     * 户口类型（0农村 1城市 2流动）
     */
    @ExcelNotation("0农村 1城市 2流动")
    @ExcelProperty(value = "户口类型", index = 17)
    @ApiModelProperty(value = "户口类型（0农村 1城市 2流动）", position = 19)
    private Integer householdType;

    /**
     * 生育状况(0已育 1未育 2已结扎 3已绝育 4不孕)
     */
    @ExcelNotation("0已育 1未育 2已结扎 3已绝育 4不孕")
    @ExcelProperty(value = "生育状况", index = 18)
    @ApiModelProperty(value = "生育状况(0已育 1未育 2已结扎 3已绝育 4不孕)", position = 20)
    private Integer fertilityStatus;

    /**
     * 联系地址
     */
    @ExcelProperty(value = "联系地址", index = 19)
    @ApiModelProperty(value = "联系地址", position = 21)
    private String address;

    /**
     * 帐号状态（0正常 1停用）
     */
    @ExcelNotation("0正常 1停用")
    @ExcelProperty(value = "帐号状态", index = 20)
    @ApiModelProperty(value = "帐号状态（0正常 1停用）", position = 22)
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @ExcelIgnore
    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）", position = 23)
    private String delFlag;

    /**
     * 最后登录IP
     */
    @ExcelIgnore
    @ApiModelProperty(value = "最后登录IP", position = 24)
    private String loginIp;

    /**
     * 最后登录时间
     */
    @ExcelIgnore
    @ApiModelProperty(value = "最后登录时间", position = 25)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginDate;


    /**
     * 年级 如:2021级
     */
    @NotNull(message = "缺少必要参数grade", groups = {ValidationGroups.Insert.class})
    @ExcelNotation("年级")
    @ApiModelProperty(value = "年级 如:2021级", position = 26)
    private Integer grade;

    /**
     * 学院名称
     */
    @ExcelProperty(value = "学院名称")
    @ApiModelProperty(value = "学院名称", position = 27)
    private String schoolName;

    /**
     * 学院编码
     */
    @ExcelProperty(value = "学院编码")
    @ApiModelProperty(value = "学院编码", position = 28)
    private String schoolNum;

    /**
     * 专业名称
     */
    @ExcelProperty(value = "专业名称")
    @ApiModelProperty(value = "专业名称", position = 29)
    private String majorName;

    /**
     * 专业编码
     */
    @ExcelProperty(value = "专业编码")
    @ApiModelProperty(value = "专业编码", position = 30)
    private String majorNum;
}
