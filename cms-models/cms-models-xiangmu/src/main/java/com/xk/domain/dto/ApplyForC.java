package com.xk.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class ApplyForC {
    /**
     * 八.财务情况
     */
    @JsonProperty("cEight")
    private String cEight;
    /**
     * 十一.风险防范
     */
    @JsonProperty("cEleven")
    private String cEleven;
    /**
     * 五.产品技术或服务内容
     */
    @JsonProperty("cFive")
    private String cFive;
    /**
     * 四.行业及市场前景
     */
    @JsonProperty("cFour")
    private String cFour;
    /**
     * 九.预期效益分析
     */
    @JsonProperty("cNine")
    private String cNine;
    /**
     * 一.实体运行机构名称或公司注册名称
     */
    @JsonProperty("cOne")
    private String cOne;
    /**
     * 七.团队组建
     */
    @JsonProperty("cSeven")
    private String cSeven;
    /**
     * 六.商业模式（创业过程、机会与商业分析）
     */
    @JsonProperty("cSix")
    private String cSix;
    /**
     * 十.投融资计划
     */
    @JsonProperty("cTen")
    private String cTen;
    /**
     * 三.项目背景
     */
    @JsonProperty("cThree")
    private String cThree;
    /**
     * 二.创业计划书摘要
     */
    @JsonProperty("cTwo")
    private String cTwo;
}
