package com.xk.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 修改延期解题
 */
@Data
@Accessors(chain = true)
public class UpdateProjectEndTimeDelayDto {
    /**
     * 项目id
     */
    @NotNull(message = "项目id不能为空")
    @Min(value = 1, message = "项目id不能小于1")
    private Long projectId;
    /**
     * 修改延期解题时间
     */
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"      // 根据你的时区调整
    )
    @Future(message = "项目结束时间不能小于当前时间")
    @NotNull(message = "项目结束时间不能为空")
    private Date projectEndTime;
}
