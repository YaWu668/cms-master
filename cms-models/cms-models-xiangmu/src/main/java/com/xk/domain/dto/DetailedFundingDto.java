package com.xk.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DetailedFundingDto {
    //开支科目
    private String name;
    //预算经费
    private String budgetFunds;
    //主要用途
    private String mainPurpose;
    //阶段下达经费计划前半阶段
    private String previousStage;
    //阶段下达经费计划后半阶段
    private String postStage;

}
