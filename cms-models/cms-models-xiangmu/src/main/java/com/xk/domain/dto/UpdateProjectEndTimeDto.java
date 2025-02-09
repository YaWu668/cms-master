package com.xk.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 修改项目结题时间
 */
@Data
@Accessors(chain = true)
public class UpdateProjectEndTimeDto {
    /**
     * 项目id
     */
    @NotNull(message = "项目id不能为空")
    @Min(value = 1, message = "项目id不能小于1")
    private Long projectId;
    /**
     * 修改解题时间
     */
    @Future(message = "项目结束时间不能小于当前时间")
    @NotNull(message = "项目结束时间不能为空")
    private Date projectEndTime;
}
