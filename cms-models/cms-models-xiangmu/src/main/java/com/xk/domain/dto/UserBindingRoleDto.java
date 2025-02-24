package com.xk.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 用户导入绑定信息
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserBindingRoleDto {

    @ExcelProperty(value = "账号(学号或者工号)", index = 0)
    private String userName;
    @ExcelProperty(value = "名字", index = 1)
    private String nickName;
}
