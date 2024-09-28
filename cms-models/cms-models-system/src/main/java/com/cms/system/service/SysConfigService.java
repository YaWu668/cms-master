package com.cms.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.system.domain.pojo.SysConfig;
import com.cms.system.domain.query.SysConfigQuery;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统参数设置服务层
 *
 * @author 邓志军
 * @date 2024年8月15日15:23:59
 */
public interface SysConfigService extends IService<SysConfig> {

    /**
     * 查询系统参数设置列表
     *
     * @param configQuery 参数查询条件
     * @return 系统参数设置列表数据
     */
    List<SysConfig> getEntityList(SysConfigQuery configQuery);

    /**
     * 根据id获取系统配置详情信息
     *
     * @param configId 配置信息id
     * @return 配置详细信息
     */
    SysConfig getEntityById(Long configId);

    /**
     * 修改系统配置详情信息
     *
     * @param config 配置详细信息
     */
    boolean updateEntity(SysConfig config);

    /**
     * 新增系统配置详情信息
     *
     * @param config 配置详细信息
     */
    boolean addEntity(SysConfig config);

    /**
     * 删除系统配置详情信息
     *
     * @param ids 系统配置id
     */
    @Transactional
    boolean deleteEntity(List<Long> ids);

    /**
     * 获取非内置配置数据
     *
     * @return 系统配置
     */
    List<Map<String, String>> getNoInternallyConfig();
}
