package com.xk.enums;

import com.cms.common.core.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 项目状态对应的评审结果
 */
@Getter
@AllArgsConstructor
public enum  ProjectResultsEnum {
    IN_PROGRESS(2L, "暂缓"),
    PENDING(3L, "暂缓"),
    PASS(4L, "合格"),
    NOT_PASS_FINAL(5L, "暂缓");

    private final long value;
    private final String description;
    public static ProjectResultsEnum fromValue(long value) {
        for (ProjectResultsEnum status : values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new ServiceException("未知的项目状态值: " + value);
    }
}
