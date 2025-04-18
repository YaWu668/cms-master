package com.xk.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xk.check.annotations.RichText;
import lombok.experimental.Accessors;

/**
 * a,b,c 三选一
 */
@lombok.Data
@Accessors(chain = true)
public class ApplyForA {
    /**
     * 七.2: 已具备的条件，尚缺少的条件及解决方法
     */
    @JsonProperty("aEight")
    @RichText(message = "七.2: 已具备的条件，尚缺少的条件及解决方法格式不正确")
    private String aEight;
    /**
     * 五.技术路线、拟解决的问题及预期成果
     */
    @JsonProperty("aFive")
    @RichText(message = "五.技术路线、拟解决的问题及预期成果格式不正确")
    private String aFive;
    /**
     * 四.创新点与项目特色
     */
    @JsonProperty("aFour")
    @RichText(message = "四.创新点与项目特色格式不正确")
    private String aFour;
    /**
     * 一.研究目的
     */
    @JsonProperty("aOne")
    @RichText(message = "一.研究目的格式不正确")
    private String aOne;
    /**
     * 七.1: 与本项目有关的研究积累和已取得的成绩
     */
    @JsonProperty("aSeven")
    @RichText(message = "七.1: 与本项目有关的研究积累和已取得的成绩格式不正确")
    private String aSeven;
    /**
     * 六.项目研究进度安排
     */
    @JsonProperty("aSix")
    @RichText
    private String aSix;
    /**
     * 三.国、内外研究现状和发展动态
     */
    @JsonProperty("aThree")
    @RichText(message = "三.国、内外研究现状和发展动态格式不正确")
    private String aThree;
    /**
     * 二.研究内容
     */
    @JsonProperty("aTwo")
    @RichText(message = "二.研究内容格式不正确")
    private String aTwo;
}
