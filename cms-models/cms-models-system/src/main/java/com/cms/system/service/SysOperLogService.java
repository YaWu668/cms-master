package com.cms.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.system.api.domain.pojo.SysOperLog;
import com.cms.system.domain.query.SysOperLogQuery;

import java.util.List;

/**
 * 操作日志记录 Service 接口
 *
 * @author 邓志军
 * @date 2024-05-29
 */
public interface SysOperLogService extends IService<SysOperLog> {

    /**
     * 查询操作日志记录列表分页数据
     *
     * @param query 查询参数
     * @return 操作日志列表
     */
    List<SysOperLog> listEntities(SysOperLogQuery query);

    /**
     * 根据id查询操作日志记录详细信息
     *
     * @param id 操作日志记录表数据id
     */
    SysOperLog getEntityById(Long id);

    /**
     * 添加操作日志记录数据
     *
     * @param sysOperLog 操作日志记录
     * @return 添加操作日志记录数据成功返回 true 否则返回 false
     */
    boolean addEntity(SysOperLog sysOperLog);
}
