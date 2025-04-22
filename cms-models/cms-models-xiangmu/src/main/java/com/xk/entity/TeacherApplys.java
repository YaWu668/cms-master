package com.xk.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 老师报名表(TeacherApplys)表实体类
 *
 * @author makejava
 * @since 2024-12-06 01:05:55
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("xm_teacher_applys")
public class TeacherApplys extends BaseEntity  {
    //老师报名表的ID
    @TableId
    private Long teacherApplyId;

    //项目表的id
    private Long projectId;
    //用户表的id
    private Long userId;
    //姓名
    private String name;
    /**
     * 老师工号
     */
    private String userName;
    //单位
    private String unit;
    //职位
    private String post;
    //联系电话
    private String phone;
    //邮箱
    private String mailbox;
    //是否指导老师(0代表是指导老师, 1代表是企业老师)
    private Integer isTeacher;
}
