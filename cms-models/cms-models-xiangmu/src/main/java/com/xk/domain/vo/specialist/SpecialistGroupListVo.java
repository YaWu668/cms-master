package com.xk.domain.vo.specialist;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xk.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 专家组表查询列表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialistGroupListVo  {
    /**
     * 专家组的id
     */
    private Long specialistGroupId;
    /**
     * 专家组的名称
     */
    private String name;

    /**
     * 状态(0正常 1停用)
     */
    private Long status;

    /**
     * 备注
     */
    private String remark;
    /**
     * 创建者
     */

    private String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
