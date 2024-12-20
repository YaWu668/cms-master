package com.xk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.exception.ServiceException;
import com.xk.entity.AuditOpinion;
import com.xk.mapper.AuditOpinionMapper;
import com.xk.service.AuditOpinionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 审核意见表(AuditOpinion)表服务实现类
 *
 * @author YaWu
 * @since 2024-12-18 02:09:46
 */
@Service("auditOpinionService")
public class AuditOpinionServiceImpl extends ServiceImpl<AuditOpinionMapper, AuditOpinion> implements AuditOpinionService {

    @Override
    public List<AuditOpinion> selectByPropertieID(Long propertieId) {
        LambdaQueryWrapper<AuditOpinion> eq = new LambdaQueryWrapper<AuditOpinion>()
                .eq(AuditOpinion::getProjectId, propertieId);
        return this.list(eq);
    }

    @Override
    public AuditOpinion getNodeByIndex(Long projectId, Long index) {
        // 查询指定项目的所有审核意见
        List<AuditOpinion> auditOpinions = this.list(
                new LambdaQueryWrapper<AuditOpinion>().eq(AuditOpinion::getProjectId, projectId));

        if (auditOpinions.isEmpty()) {
            throw new ServiceException("项目id:" + projectId + " 的审核意见记录表不存在，请先初始化项目",400);
        }

        // 构建以 audit_opinion_id 为键的映射表
        Map<Long, AuditOpinion> opinionMap = auditOpinions.stream()
                .collect(Collectors.toMap(AuditOpinion::getAuditOpinionId, opinion -> opinion));

        // 找到根节点
        AuditOpinion current = auditOpinions.stream()
                .filter(opinion -> opinion.getRootId() == -1)
                .findFirst()
                .orElseThrow(() -> new ServiceException("项目id:" + projectId + " 的根节点未找到",400));

        // 遍历链表找到指定索引的节点
        Long currentIndex = 1L;
        while (current != null && opinionMap.containsKey(current.getAuditOpinionId())) {
            if (currentIndex.equals(index)) {
                return current;
            }
            Long nextId = current.getAuditOpinionId();
            AuditOpinion next = auditOpinions.stream()
                    .filter(opinion -> opinion.getRootId().equals(nextId))
                    .findFirst()
                    .orElse(null);

            current = next;
            currentIndex++;
        }
        // 如果遍历结束未找到索引对应的节点
        throw new ServiceException("项目id:" + projectId + " 的索引为" + index + "的节点未找到",400);
    }


    @Override
    public AuditOpinion getLastByProjectId(Long projectId) {
        // 查询指定项目的所有审核意见
        List<AuditOpinion> auditOpinions = this.list(
                new LambdaQueryWrapper<AuditOpinion>().eq(AuditOpinion::getProjectId, projectId));

        if (auditOpinions.isEmpty()) {
            throw new ServiceException("项目id:" + projectId + " 的审核意见记录表不存在，请先初始化项目",400);
        }

        // 构建以 audit_opinion_id 为键的映射表
        Map<Long, AuditOpinion> opinionMap = auditOpinions.stream()
                .collect(Collectors.toMap(AuditOpinion::getAuditOpinionId, opinion -> opinion));

        // 找到根节点
        AuditOpinion current = auditOpinions.stream()
                .filter(opinion -> opinion.getRootId() == -1)
                .findFirst()
                .orElseThrow(() -> new ServiceException("项目id:" + projectId + " 的根节点未找到",400));

        // 循环根据父节点找到下一个节点，直到没有下一个节点为止
        while (current != null && opinionMap.containsKey(current.getAuditOpinionId())) {
            Long nextId = current.getAuditOpinionId();
            AuditOpinion next = auditOpinions.stream()
                    .filter(opinion -> opinion.getRootId().equals(nextId))
                    .findFirst()
                    .orElse(null);

            if (next == null) {
                break;
            }
            current = next;
        }
        return current;
    }

    @Override
    public boolean isProjectAudit(Long projectId) {
        LambdaQueryWrapper<AuditOpinion> eq = new LambdaQueryWrapper<AuditOpinion>()
                .eq(AuditOpinion::getProjectId, projectId);
        long count = this.count(eq);
        if (count > 0) {
            return true;
            }
        return false;
    }
}

