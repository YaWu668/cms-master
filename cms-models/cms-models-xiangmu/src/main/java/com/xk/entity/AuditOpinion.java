package com.xk.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.experimental.Accessors;

/**
 * 审核意见表(AuditOpinion)表实体类
 *
 * @author YaWu
 * @since 2024-12-18 02:09:46
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("xm_audit_opinion")
@Accessors(chain = true)
public class AuditOpinion  {
    //审核意见的ID
    @TableId
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
    //创建者
    private String createBy;
    //创建时间
    private Date createTime;
    //更新者
    private String updateBy;
    //更新时间
    private Date updateTime;

}
