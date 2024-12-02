package com.xk.domain.dto;

/**
 * a,b,c 三选一
 */
@lombok.Data
public class ApplyForB {
    /**
     * 八.效益预测
     */
    private String bEight;
    /**
     * 五.投融资方案
     */
    private String bFive;
    /**
     * 四.生产或运营
     */
    private String bFour;
    /**
     * 一.项目来源
     */
    private String bOne;
    /**
     * 七. 风险预测及应对措施
     */
    private String bSeven;
    /**
     * 六.管理模式
     */
    private String bSix;
    /**
     * 三.创新点与项目特色
     */
    private String bThree;
    /**
     * 二.行业及市场前景
     */
    private String bTwo;
}
