package com.xk.domain.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xk.domain.query.PageQuery;
import com.xk.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 新增专家人员表(SpecialistData)表实体类
 *
 * @author YaWu
 * @since 2024-12-19 01:42:02
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddSpecialistDataDto {
    /**
     * 专家组的id
     */
    @NotNull(message = "专家组的id不能为空")
    @Min(value = 1, message = "专家组的id不能小于1")
    private Long specialistGroupId;
    /**
     * 用户的id
     */
    @NotNull(message = "用户的id不能为空")
    @Min(value = 1, message = "用户的id不能小于1")
    private Long userId;
    /**
     * 专家的手机号
     */
    @NotNull(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @NotEmpty(message = "手机号不能为空")
    private String phonenumber;


}
