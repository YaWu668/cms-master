package com.xk.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 该类是用于详细资金信息的数据传输对象
 * 它封装了与资金开支相关的各种信息，方便在不同层之间传输数据。
 */
@Data
@Accessors(chain = true)
public class DetailedFundingDto {
    /**
     * 比如“1”，“2”，“3”
     */
    private String id;
    /**
     * 顶级行为为null；子项行填它最近的父行id
     */
    private String parentId;
    /**
     * 开支科目，用于明确资金的具体用途分类。
     */
    private String name;
    /**
     * 预算经费，即预先规划好的该开支科目所对应的资金额度。
     */
    private String budgetFunds;
    /**
     * 主要用途，详细描述该开支科目的主要使用方向。
     */
    private String mainPurpose;
    /**
     * 阶段下达经费计划前半阶段，指在某个阶段内前期所下达的经费额度。
     */
    private String previousStage;
    /**
     * 阶段下达经费计划后半阶段，指在某个阶段内后期所下达的经费额度。
     */
    private String postStage;
}
