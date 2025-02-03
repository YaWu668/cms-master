package com.xk.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 学院组表新增实体类
 *
 * @author yauw
 * @since 2024-12-05 21:21:54
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AddCollegeGroup {
    /**
     * 学院组名称
     */
    @NotNull(message = "学院组名称不能为空")
    @Length(min= 3,max = 50, message = "学院组名称长度3~50以内")
    @NotEmpty(message = "学院组名称不能为空")
    private String name;
    /**
     * 备注
     */
    private String remark;
}
