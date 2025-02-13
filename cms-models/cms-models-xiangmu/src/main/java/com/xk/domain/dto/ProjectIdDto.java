package com.xk.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 根据项目id查询详细
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProjectIdDto {
    /**
     * 项目id
     */
    @NotNull(message = "项目id不能为空")
    @Min(value = 1,message = "项目id不能小于1")
    private Long id;

    /**
     * 项目角色标识符
     */
    private String type;

}
