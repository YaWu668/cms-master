package com.xk.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 专家组表SpecialistGroup实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("xm_specialist_group")
public class SpecialistGroup  extends BaseEntity{
    //专家组的id
    @TableId
    private Long specialistGroupId;

    //专家组的名称
    private String name;

    //状态(0正常 1停用)
    private String status;

    //备注
    private String remark;

}
