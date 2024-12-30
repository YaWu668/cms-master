package com.xk.domain.vo.group;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
/**
 * 年度组表Vo类
 *
 * @author makejava
 * @since 2024-12-05 22:47:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class YearGroupVo  {
    //年度组的id
    private Long yearGroupId;
    //年度组的名称
    private String name;
    //备注
    private String remark;
}
