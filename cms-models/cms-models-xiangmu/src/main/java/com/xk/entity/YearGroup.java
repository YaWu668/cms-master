package com.xk.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 年度组表(YearGroup)表实体类
 *
 * @author makejava
 * @since 2024-12-05 22:47:31
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("xm_year_group")
public class YearGroup  {
    //年度组的id
    @TableId
    private Long yearGroupId;

    //年度组的名称
    private String name;
    //状态(0正常 1停用)
    private Integer status;
    //备注
    private String remark;
    //创建者
    private String createBy;
    //创建时间
    private Date createTime;
    //更新者
    private String updateBy;
    //更新时间
    private Date updateTime;



}
