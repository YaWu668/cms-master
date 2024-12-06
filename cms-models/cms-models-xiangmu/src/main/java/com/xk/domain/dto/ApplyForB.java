package com.xk.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * a,b,c 三选一
 */
@lombok.Data
public class ApplyForB {
    /**
     * 八.效益预测
     */
    @JsonProperty("bEight")
    private String bEight;
    /**
     * 五.投融资方案
     */
    @JsonProperty("bFive")
    private String bFive;
    /**
     * 四.生产或运营
     */
    @JsonProperty("bFour")
    private String bFour;
    /**
     * 一.项目来源
     */
    @JsonProperty("bOne")
    private String bOne;
    /**
     * 七. 风险预测及应对措施
     */
    @JsonProperty("bSeven")
    private String bSeven;
    /**
     * 六.管理模式
     */
    @JsonProperty("bSix")
    private String bSix;
    /**
     * 三.创新点与项目特色
     */
    @JsonProperty("bThree")
    private String bThree;
    /**
     * 二.行业及市场前景
     */
    @JsonProperty("bTwo")
    private String bTwo;
}
