package com.xk.domain.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 新增年度
 */
@Data
public class AddYearGroup {
    /**
     * 年度组的名称,限制长度: 5-30
     */
    @NotNull(message = "年度组名称不能为空")
    @Length(min = 5,max = 30, message = "年度组名字长度:5~30")
    private String name;
    //备注
    private String remark;

}
