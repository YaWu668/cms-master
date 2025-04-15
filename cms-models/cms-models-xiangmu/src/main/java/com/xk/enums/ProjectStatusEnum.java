package com.xk.enums;

import com.cms.common.core.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectStatusEnum {
    NOT_PASS(0L, "未通过审核"),
    AUDITING(1L, "审核中"),
    IN_PROGRESS(2L, "项目进行中"),
    PENDING(3L, "待结题"),
    PASS(4L, "通过结题"),
    NOT_PASS_FINAL(5L, "不通过结题");

    private final long value;
    private final String description;

    // 根据 value 获取枚举
    public static ProjectStatusEnum fromValue(long value) {
        for (ProjectStatusEnum status : values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new ServiceException("未知的项目状态值: " + value);
    }
}
