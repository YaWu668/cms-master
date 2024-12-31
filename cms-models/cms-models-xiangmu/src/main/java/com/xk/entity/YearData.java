package com.xk.entity;

import java.util.Date;

import java.io.Serializable;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class YearData extends BaseEntity{
    /**
     * 年度数据的id
     */
    @TableId
    private Long yearDataId;
    /**
     * 年度组的id
     */
    private Long yearGroupId;
    /**
     * 年度数据的名称
     */
    private String name;
    /**
     * 年度开始时间 格式 yyyy-MM-dd HH:mm:ss
     */
    @JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date begin;
    /**
     * 年度结束时间
     */
    @JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date end;
}
