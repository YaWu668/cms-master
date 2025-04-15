package com.xk.domain.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 提交解题文件
 */
@Data
public class SubmitFileDTO {
    /**
     * 结题文件URL
     */
    @NotBlank(message = "文件URL不能为空")
    private String fileURL;
    /**
     * 项目id
     */
    @NotNull(message = "项目id不能为空")
    @Min(value = 1, message = "项目id不能小于1")
    private Long problemId;
}
