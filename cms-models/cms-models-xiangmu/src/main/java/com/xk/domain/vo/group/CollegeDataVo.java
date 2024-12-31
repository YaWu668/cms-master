package com.xk.domain.vo.group;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xk.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学院组人员表的vo类
 */
@Data
public class CollegeDataVo  {
    /**
     * 学院组的id
     */
    private Long collegeGroupId;
    /**
     * 用户的id
     */
    private Long userId;
    /**
     * 学院人员账号
     */
    private String userName;
    /**
     * 学院人员手机号
     */
    private String phonenumber;

}
