package com.cms.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.system.api.domain.pojo.SysOperLog;
import com.cms.system.domain.query.SysOperLogQuery;
import com.cms.system.mapper.SysOperLogMapper;
import com.cms.system.service.SysOperLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 操作日志记录 Service 实现
 *
 * @author 邓志军
 * @date 2024-05-29
 */
@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements SysOperLogService {

    @Resource
    private SysOperLogMapper operLogMapper;

    /**
     * 查询操作日志记录列表分页数据
     *
     * @param query 查询参数
     * @return 操作日志列表
     */
    @Override
    public List<SysOperLog> listEntities(SysOperLogQuery query) {
        return this.operLogMapper.listEntities(query);
    }

    /**
     * 根据id查询操作日志记录详细信息
     *
     * @param id 操作日志记录表数据id
     */
    @Override
    public SysOperLog getEntityById(Long id) {
        return this.getById(id);
    }

    /**
     * 添加操作日志记录数据
     *
     * @param sysOperLog 操作日志记录
     * @return 添加操作日志记录数据成功返回 true 否则返回 false
     */
    @Override
    public boolean addEntity(SysOperLog sysOperLog) {
        return this.save(sysOperLog);
    }
}
