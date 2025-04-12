package com.xk.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 学生报名表(StudnetApplys)表实体类
 *
 * @author makejava
 * @since 2024-12-06 00:37:22
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("xm_studnet_applys")
public class StudnetApplys  extends BaseEntity {
    //学生报名表的ID
    @TableId
    private Long studnetApplyId;

    //项目表的id
    private Long projectId;
    /**
     * 用户表的id
     */
    private Long userId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别(字典)
     */
    private Long sex;
    /**
     * 学号
     */
    private String userName;
    /**
     * 民族(字典)
     */
    private Long nation;
    /**
     * 出生年月(格式:yyyy-rr-xxx)
     */
    private String dateOfBirth;
    /**
     * 学院(学院组的id)
     */
    private Long collegeGroupId;
    /**
     * 专业班级,(用户自己输入)
     */
    private String professionalClass;
    /**
     * 联系电话,(用户自己输入)
     */
    private String phone;
    /**
     * 手机(不知道搞什么的)
     */
    private String mobilePhone;
    /**
     * 邮箱(用户自己输入)
     */
    private String mailbox;
    /**
     * 项目中的分工(用户自己输入)
     */
    private String job;
    /**
     * 是否负责人 , 0为不是, 1是
     */
    private Integer isPrincipal;
}
