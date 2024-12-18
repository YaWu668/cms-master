package com.xk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xk.entity.AuditOpinion;
import com.xk.mapper.AuditOpinionMapper;
import com.xk.service.AuditOpinionService;
import org.springframework.stereotype.Service;

/**
 * 审核意见表(AuditOpinion)表服务实现类
 *
 * @author YaWu
 * @since 2024-12-18 02:09:46
 */
@Service("auditOpinionService")
public class AuditOpinionServiceImpl extends ServiceImpl<AuditOpinionMapper, AuditOpinion> implements AuditOpinionService {

}

