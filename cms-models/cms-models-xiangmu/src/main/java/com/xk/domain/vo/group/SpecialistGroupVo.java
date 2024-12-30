package com.xk.domain.vo.group;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 专家组表vo类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialistGroupVo {
    //专家组的id

    private Long specialistGroupId;

    //专家组的名称
    private String name;

    //备注
    private String remark;
}
