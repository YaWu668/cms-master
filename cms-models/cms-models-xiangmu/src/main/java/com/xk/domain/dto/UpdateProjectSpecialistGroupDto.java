package com.xk.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 根据项目id集合进行批量分配专家组
 */
@Data
public class UpdateProjectSpecialistGroupDto {
    /**
     * 项目id集合
     */
    @NotNull(message = "项目id集合不能为空")
    @Size(min = 1, message = "项目id集合不能为空")
    private List<Long> projectIds;
    /**
     * 专家组id
     */
    @NotNull(message = "专家组id不能为空")
    private Long specialistGroupId;

}
