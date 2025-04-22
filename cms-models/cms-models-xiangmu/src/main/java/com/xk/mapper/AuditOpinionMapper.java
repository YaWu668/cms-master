package com.xk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xk.domain.dto.AuditOpinionDTO;
import com.xk.entity.AuditOpinion;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 审核意见表(AuditOpinion)表数据库访问层
 *
 * @author YaWu
 * @since 2024-12-18 02:09:46
 */
public interface AuditOpinionMapper extends BaseMapper<AuditOpinion> {

    @Select("SELECT\n" +
                    "    a.audit_opinion_id,\n" +
                    "    a.project_id,\n" +
                    "    a.user_id,\n" +
                    "    a.role_id,\n" +
                    "    a.audit_opinion,\n" +
                    "    r.role_key\n" +
                    "FROM\n" +
                    "    xm_audit_opinion AS a\n" +
                    "    INNER JOIN sys_role AS r ON a.role_id = r.role_id\n" +
                    "WHERE\n" +
                    "    a.audit_state != 1\n" +
                    "    AND a.project_id = #{projectId}\n" +
                    "\t\tAND r.role_key = 'xm-admin';")
    AuditOpinionDTO getAuditOpinionDTOList(@Param("projectId") Long projectId);
}

