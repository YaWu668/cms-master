package com.xk.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 年度组表(YearData)表实体类
 *
 * @author makejava
 * @since 2024-12-05 22:52:49
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("xm_year_data")
public class YearData  {
    //年度数据的id
    @TableId
    private Long yearDataId;

    //年度类型的id
    private Long yearGroupId;
    //年度数据的名称
    private String name;
    //年度开始时间 格式 xxxx-yy-xx
    private Date begin;
    //年度结束时间
    private Date end;
    //创建者
    private String createBy;
    //创建时间
    private Date createTime;
    //更新者
    private String updateBy;
    //更新时间
    private Date updateTime;



}
