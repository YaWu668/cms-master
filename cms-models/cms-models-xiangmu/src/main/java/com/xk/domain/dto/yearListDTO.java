package com.xk.domain.dto;

import com.xk.entity.YearData;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 年度组和年度数据列表DTO
 */
@lombok.Data
@Accessors(chain = true)
public class yearListDTO {
    /**
     * 年度组的名称
     */
    private String name;
    /**
     * 年度组的id
     */
    private Long yearGroupId;
    /**
     * 年度数据的列表
     */
    private List<YearDataDto> yearDatas;
}

