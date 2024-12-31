package com.xk.entity;

import java.util.Date;

import java.io.Serializable;
import java.util.List;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.w3c.dom.stylesheets.LinkStyle;

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
public class YearGroup extends BaseEntity {
    //年度组的id
    @TableId
    private Long yearGroupId;

    //年度组的名称
    private String name;
    //状态(0正常 1停用)
    private Integer status;
    //备注
    private String remark;
    /**
     * 嵌套接收年度数据
     */
    // 非数据库字段
    @TableField(exist = false)
    private List<YearData> yearData;
}
