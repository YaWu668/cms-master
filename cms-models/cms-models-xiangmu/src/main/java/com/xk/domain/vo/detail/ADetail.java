package com.xk.domain.vo.detail;

import lombok.experimental.Accessors;

/**
 * a,b,c 三选一
 */
@lombok.Data
@Accessors(chain = true)
public class ADetail {
    /**
     * 七.2: 已具备的条件，尚缺少的条件及解决方法
     */
    private String aEight;
    /**
     * 五.技术路线、拟解决的问题及预期成果
     */
    private String aFive;
    /**
     * 四.创新点与项目特色
     */
    private String aFour;
    /**
     * 一.研究目的
     */
    private String aOne;
    /**
     * 七.1: 与本项目有关的研究积累和已取得的成绩
     */
    private String aSeven;
    /**
     * 六.项目研究进度安排
     */
    private String aSix;
    /**
     * 三.国、内外研究现状和发展动态
     */
    private String aThree;
    /**
     * 二.研究内容
     */
    private String aTwo;
}
