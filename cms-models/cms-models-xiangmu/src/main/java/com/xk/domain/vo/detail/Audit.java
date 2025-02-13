package com.xk.domain.vo.detail;

import com.xk.config.XMAuditConfig;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 审核和项目进度
 */
@lombok.Data
@Accessors(chain = true)
public class Audit {
    /**
     * 审核意见
     */
    private List<Opinion> opinion;
    /**
     * 项目进度
     */
    private List<Schedule> schedule;
    /**
     * 当前项目需要需要审核角色的顺序
     */
    private List<XMAuditConfig.AuditRole> auditRoles;
    /**
     * 项目状态
     */
    private Long state;
    /**
     * 审核进度从1开始一次递增,0为审核完毕
     */
    private Long auditProgress;
}
