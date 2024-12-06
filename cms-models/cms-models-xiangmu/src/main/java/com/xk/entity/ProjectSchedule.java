package com.xk.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 项目进度记录表(ProjectSchedule)表实体类
 *
 * @author makejava
 * @since 2024-12-07 01:10:18
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("xm_project_schedule")
@lombok.experimental.Accessors(chain = true)
public class ProjectSchedule  {
    //项目进度id
    @TableId
    private Long projectScheduleId;

    //项目表的id
    private Long projectId;
    //项目进度表的上一级进度的id, -1为项目初始化申请状态
    private Long rootId;
    //记录项目修改项目当前状态的用户id
    private Long userId;
    //内容,就记录项目当前状态内容
    private String content;
    //创建者
    private String createBy;
    //创建时间
    private Date createTime;
    //更新者
    private String updateBy;
    //更新时间
    private Date updateTime;



}
