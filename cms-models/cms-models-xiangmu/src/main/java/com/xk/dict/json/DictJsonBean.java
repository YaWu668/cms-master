package com.xk.dict.json;

@lombok.Data
public class DictJsonBean {
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 字典变化
     */
    private long dictCode;
    /**
     * 字典标签名称
     */
    private String dictLabel;
    /**
     * 字典排序
     */
    private long dictSort;
    /**
     * 字典值
     */
    private long dictValue;
    /**
     * 是否默认(没有用字段)
     */
    private String isDefault;
    /**
     * 样式(没有用的字段)
     */
    private String listClass;
    /**
     * 状态(0正常,1停用)
     */
    private String status;
    /**
     * 类型
     */
    private String type;
}
