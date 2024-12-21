package com.xk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xk.entity.AuditOpinion;

import java.util.List;


/**
 * 审核意见表(AuditOpinion)表服务接口
 *
 * @author YaWu
 * @since 2024-12-18 02:09:46
 */
public interface AuditOpinionService extends IService<AuditOpinion> {

    /**
     * 根据项目ID查询审核意见
     * @param propertieId 项目ID
     * @return 审核意见列表, 若无则返回空列表
     */
    List<AuditOpinion>  selectByPropertieID(Long propertieId);


    /**
     * 根据索引获取指定的节点
     * @param projectId 项目ID
     * @param index 要获取的节点索引（1表示第一个节点）
     * @return 指定的节点
     */
     AuditOpinion getNodeByIndex(Long projectId, Long index);


    /**
     * 根据项目ID获取最后一个节点
     * @param projectId 项目ID
     * @return 最后一个节点
     */
     AuditOpinion getLastByProjectId(Long projectId) ;

    /**
     * 根据项目ID判断项目是否有审核记录
     * @param projectId 项目ID
     * @return true表示有审核记录，false表示没有
     */
    boolean isProjectAudit(Long projectId);

    /**
     * 根据项目ID判断项目是否有审核记录
     * @param projectId 项目ID
     * @return true表示没有审核记录，false表示有
     */
    boolean isEmptyProjectAudit(Long projectId);
}

