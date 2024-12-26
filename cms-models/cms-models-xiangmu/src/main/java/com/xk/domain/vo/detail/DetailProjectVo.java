package com.xk.domain.vo.detail;

import lombok.experimental.Accessors;

import java.util.List;

/**
 * 项目详细信息
 */
@lombok.Data
@Accessors(chain = true)
public class DetailProjectVo {
    /**
     * 立项依据
     */
    private According according;
    /**
     * 审核和项目进度
     */
    private Audit audit;
    /**
     * 结题文件
     */
    private List<String> concludeurl;
    /**
     * 基本情况
     */
    private Elementary elementary;
    /**
     * 预算
     */
    private Expenditure expenditure;
    /**
     * 项目附加的URL地址, 提交只能提交压缩包
     */
    private String materialsurl;
    /**
     * 成员和老师
     */
    private Personnels personnels;
}
