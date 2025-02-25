package com.xk.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 导入用户信息绑定
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class importUserDto {
    /**
     * 账号(学号或者工号)
     */
    @ExcelProperty(value = "账号(学号或者工号)", index = 0)
    private String userName;
    /**
     * 昵称
     */
    @ExcelProperty(value = "名字", index = 1)
    private String nickName;
    /**
     * 性别  0男 1女 2未知
     */
    @ExcelProperty(value = "性别", index = 2)
    private String sex;
}
