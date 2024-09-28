package com.cms.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.constant.CacheConstants;
import com.cms.common.redis.service.RedisService;
import com.cms.system.domain.pojo.SysConfig;
import com.cms.system.domain.query.SysConfigQuery;
import com.cms.system.mapper.SysConfigMapper;
import com.cms.system.service.SysConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

/**
 * 系统参数设置服务层
 *
 * @author 邓志军
 * @date 2024年8月15日15:26:28
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {

    @Resource
    private SysConfigMapper sysConfigMapper;

    @Resource
    private RedisService redisService;

    @PostConstruct
    public void initConfig() {
        this.reloadRedisConfigData();
    }

    /**
     * 查询系统参数设置列表
     *
     * @param configQuery 参数查询条件
     * @return 系统参数设置列表数据
     */
    @Override
    public List<SysConfig> getEntityList(SysConfigQuery configQuery) {
        return this.sysConfigMapper.getEntityList(configQuery);
    }

    /**
     * 根据id获取系统配置详情信息
     *
     * @param configId 配置信息id
     * @return 配置详细信息
     */
    @Override
    public SysConfig getEntityById(Long configId) {
        return this.getById(configId);
    }

    /**
     * 修改系统配置详情信息
     *
     * @param config 配置详细信息
     */
    @Override
    public boolean updateEntity(SysConfig config) {
        // 1.更改数据库数据
        this.updateById(config);
        // 2.重新缓存redis数据
        return this.reloadRedisConfigData();
    }

    /**
     * 新增系统配置详情信息
     *
     * @param config 配置详细信息
     */
    @Override
    public boolean addEntity(SysConfig config) {
        // 1.新增数据库数据
        this.save(config);
        // 2.重新缓存redis数据
        return this.reloadRedisConfigData();
    }

    /**
     * 删除系统配置详情信息
     *
     * @param ids 系统配置id
     */
    @Override
    public boolean deleteEntity(List<Long> ids) {
        // 1.删除数据库中数据
        this.sysConfigMapper.deleteEntity(ids);
        // 2.重新缓存redis数据
        return this.reloadRedisConfigData();
    }

    /**
     * 获取非内置配置数据
     *
     * @return 系统配置
     */
    @Override
    public List<Map<String, String>> getNoInternallyConfig() {
        // 1.查询缓存中所有的配置
        Collection<String> keys = redisService.keys(CacheConstants.SYS_CONFIG_KEY + "*");

        // 2.组装数据
        List<Map<String,String>> resultMap = new ArrayList<>();
        keys.forEach(key -> {
            SysConfig sysConfig = redisService.getCacheObject(key);
            if ("N".equals(sysConfig.getConfigType())) {
                HashMap<String, String> map = new HashMap<>();
                map.put(sysConfig.getConfigKey(),sysConfig.getConfigValue());
                resultMap.add(map);
            }
        });

        // 3.返回数据
        return resultMap;
    }

    /**
     * 重新装栽 redis 缓存数据
     *
     * @return 处理结果
     */
    private boolean reloadRedisConfigData() {
        // 1.查询所有数据
        List<SysConfig> configList = this.getEntityList(new SysConfigQuery());
        // 2.删除redis中所有缓存数据
        Collection<String> keys = redisService.keys(CacheConstants.SYS_CONFIG_KEY + "*");
        redisService.deleteObject(keys);
        // 3.将数据缓存到redis中
        configList.forEach(config -> redisService.setCacheObject(this.buildCacheKey(config), config));
        // 4.返回结果
        return true;
    }

    /**
     * 构建缓存的key
     *
     * @param config 配置文件对象
     * @return 缓存key
     */
    private String buildCacheKey(SysConfig config) {
        return CacheConstants.SYS_CONFIG_KEY + config.getConfigKey();
    }
}
