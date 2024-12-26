package com.xk.domain.vo.detail;

import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 预算
 */
@lombok.Data
@Accessors(chain = true)
public class Expenditure {
    /**
     * 详细经费预算, 没有设计业务, 前端发什么样子json, 就存储什么样子
     */
    private String budget;
    /**
     * 财政拨款的金额(钱)
     */
    private BigDecimal fiscalAppropriation;
    /**
     * 学校拨款的金额
     */
    private BigDecimal schoolAllocation;
    /**
     * 申请金额合计
     */
    private BigDecimal totalMoney;
}
