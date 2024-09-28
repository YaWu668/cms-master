package com.cms.system.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通知公告查询对象
 *
 * @author 邓志军
 * @date 2024年8月25日08:01:47
 */
@ApiModel(description = "通知公告查询对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysNoticeQuery {

    /**
     * 公告标题
     */
    @ApiModelProperty(value = "公告标题", position = 1)
    private String noticeTitle;

    /**
     * 公告类型（1通知 2公告）
     */
    @ApiModelProperty(value = "公告类型（1通知 2公告）", position = 2)
    private String noticeType;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者", position = 3)
    private String createBy;
}
