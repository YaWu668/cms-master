package com.xk.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 审核意见表dto,多加一个角色表的角色权限字符串
 */
@Data
@TableName("xm_audit_opinion")
public class AuditOpinionDTO {
    //审核意见表的id
    private Long auditOpinionId;
    //项目表的id
    private Long projectId;
    //用户表的id, 进行审核的用户的id
    private Long userId;
    //用户当时进行审核的角色id
    private Long roleId;
    //审核意见表的上一级的id(链表结构), -1为第一个审核的角色
    private Long rootId;
    //审核状态(0表示通过, 1表示不通过)
    private Long auditState;
    //审核意见
    private String auditOpinion;
    /**
     * 角色表字段,
     * 角色权限字符串
     */
    @TableField(exist = false)
    private String roleKey;
}
