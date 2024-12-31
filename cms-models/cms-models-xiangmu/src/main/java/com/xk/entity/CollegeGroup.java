package com.xk.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 学院组表(CollegeGroup)表实体类
 *
 * @author yauw
 * @since 2024-12-05 21:21:54
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("xm_college_group")
public class CollegeGroup extends   BaseEntity  {
    //学院组的id
    @TableId
    private Long collegeGroupId;
    //学院组名称
    private String name;
    //状态(0正常 1停用)
    private Integer status;
    //备注
    private String remark;
}
