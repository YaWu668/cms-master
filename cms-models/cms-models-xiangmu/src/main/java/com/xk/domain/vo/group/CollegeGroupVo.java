package com.xk.domain.vo.group;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
/**
 * 学院组表字典VO
 *
 * @author yauw
 * @since 2024-12-05 21:21:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollegeGroupVo  {
    //学院组的id
    private Long collegeGroupId;
    //学院组名称
    private String name;
    //备注
    private String remark;
}
