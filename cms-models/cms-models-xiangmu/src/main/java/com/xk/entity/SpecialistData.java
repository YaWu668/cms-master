package com.xk.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 专家人员表(SpecialistData)表实体类
 *
 * @author YaWu
 * @since 2024-12-19 01:42:02
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("xm_specialist_data")
public class SpecialistData  extends BaseEntity {
    //专家人员id
    @TableId
    private Long specialistDataId;

    //专家组的id
    private Long specialistGroupId;
    //用户的id
    private Long userId;
    //专家的账号
    private String userName;
    //专家的手机号
    private String phonenumber;


}
