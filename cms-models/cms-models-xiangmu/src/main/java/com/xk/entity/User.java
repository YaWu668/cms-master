package com.xk.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 用户信息表(User)表实体类
 *
 * @author makejava
 * @since 2024-12-05 01:01:13
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user")
public class User  {
    //用户ID
    @TableId
    private Long userId;

    //用户账号, (学生就学号, 老师就是工号)
    private String userName;
    //密码
    private String password;
    //用户姓名,学生姓名或者教师姓名
    private String nickName;
    //帐号状态（0正常 1停用）
    private String status;
    //用户类型（00系统用户）
    private String userType;
    //用户邮箱
    private String email;
    //手机号码
    private String phonenumber;
    //用户性别（0男 1女 2未知）
    private String sex;
    //头像地址
    private String avatar;
    //年级 如:2021级
    private Integer grade;
    //学院名称
    private String schoolName;
    //学院编码
    private String schoolNum;
    //专业名称
    private String majorName;
    //专业编码
    private String majorNum;
    //删除标志（0代表存在 1代表删除）
    @TableLogic(value = "0", delval = "1")
    private String delFlag;
    //最后登录IP
    private String loginIp;
    //最后登录时间
    private Date loginDate;
    //创建者
    private String createBy;
    //创建时间
    private Date createTime;
    //更新者
    private String updateBy;
    //更新时间
    private Date updateTime;
    //备注
    private String remark;
    //部门ID
    private Long deptId;
    //入职日期
    private Date dateOfHire;
    //上级领导
    private Long leader;
    //人员类型(0 合同员 1派遣工 2实习生 3临时工)
    private String personnelType;
    //身份证
    private String idNumber;
    //微信账号
    private String weChatId;
    //QQ账号
    private String qqId;
    //出生日期
    private Date birthday;
    //婚姻状况(0未婚 1已婚 2离异 3丧偶)
    private String maritalStatus;
    //户口类型（0农村 1城市 2流动）
    private String householdType;
    //联系地址
    private String address;
    //生育状况(0已育 1未育 2已结扎 3已绝育 4不孕)
    private String fertilityStatus;



}
