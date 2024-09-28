package com.cms.system.mapper;

import com.cms.system.api.domain.pojo.SysOperLog;
import com.cms.system.domain.query.SysOperLogQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 操作日志记录Mapper
 *
 * @author 邓志军
 * @date 2024-05-29
 */
public interface SysOperLogMapper extends BaseMapper<SysOperLog> {

    /**
     * 查询操作日志记录列表分页数据
     *
     * @param query 查询参数
     * @return 操作日志列表
     */
    List<SysOperLog> listEntities(SysOperLogQuery query);
}
