package com.xk.domain.dto;

import lombok.experimental.Accessors;

/**
 * 学院列表dto
 */
@lombok.Data
@Accessors(chain = true)
public class collegeListDto{
    /**
     * 学院id
     */
    private long collegeGroupId;
    /**
     * 学院名称
     */
    private String name;
}
