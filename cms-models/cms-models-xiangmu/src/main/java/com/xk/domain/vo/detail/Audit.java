package com.xk.domain.vo.detail;

import lombok.experimental.Accessors;

import java.util.List;

/**
 * 审核和项目进度
 */
@lombok.Data
@Accessors(chain = true)
public class Audit {
    /**
     * 审核意见
     */
    private List<Opinion> opinion;
    /**
     * 项目进度
     */
    private List<Schedule> schedule;
    /**
     * 项目状态
     */
    private Long state;
}
