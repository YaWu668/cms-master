package com.xk.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 学院组人员表(CollegeData)表实体类
 *
 * @author YaWu
 * @since 2024-12-19 00:50:30
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("xm_college_data")
public class CollegeData  extends  BaseEntity {
    //学院人员id
    @TableId
    private Long collegeDataId;

    //学院组的id
    private Long collegeGroupId;
    //用户的id
    private Long userId;
    //学院人员账号
    private String userName;
    //学院人员手机号
    private String phonenumber;

}
