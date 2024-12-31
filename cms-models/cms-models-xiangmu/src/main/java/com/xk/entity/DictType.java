package com.xk.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 字典类型表(DictType)表实体类
 *
 * @author makejava
 * @since 2024-12-06 00:06:48
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_dict_type")
public class DictType extends  BaseEntity  {
    //字典主键
    @TableId
    private Long dictId;

    //字典名称
    private String dictName;
    //字典类型
    private String dictType;
    //状态（0正常 1停用）
    private String status;
    //备注
    private String remark;



}
