package com.cms.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.utils.StringUtils;
import com.cms.common.security.utils.DictUtils;
import com.cms.system.api.domain.pojo.SysDictData;
import com.cms.system.domain.query.SysDictDataQuery;
import com.cms.system.mapper.SysDictDataMapper;
import com.cms.system.service.SysDictDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统字典数据
 *
 * @author 邓志军
 * @date 2024年6月1日18:32:50
 */
@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements SysDictDataService {

    @Resource
    private SysDictDataMapper sysDictDataMapper;

    /**
     * 获取系统字典值数据列表
     *
     * @param dictType 字典类型
     * @param query    查询条件
     * @return 系统字典值数据列表
     */
    @Override
    public List<SysDictData> listEntities(String dictType, SysDictDataQuery query) {
        return this.sysDictDataMapper.listEntities(dictType, query);
    }

    /**
     * 添加系统字典数据
     *
     * @param sysDictData 字典数据详情
     */
    @Override
    public boolean addEntity(SysDictData sysDictData) {
        if (this.save(sysDictData)) {
            String dictType = sysDictData.getDictType();
            // 获取新的字典数据
            List<SysDictData> dictData = this.listEntities(dictType, new SysDictDataQuery());
            // 重新写入缓存
            if (StringUtils.isNotEmpty(dictData)) {
                DictUtils.setDictCache(dictType, dictData);
            }
            return true;
        }
        return false;
    }

    /**
     * 修改系统字典数据
     *
     * @param sysDictData 字典数据详情
     */
    @Override
    public boolean updateEntity(SysDictData sysDictData) {
        // 判断是否修改成功
        if (this.updateById(sysDictData)) {
            String dictType = sysDictData.getDictType();
            // 获取新的字典数据
            List<SysDictData> dictData = this.listEntities(dictType, new SysDictDataQuery());
            // 重新写入缓存
            if (StringUtils.isNotEmpty(dictData)) {
                DictUtils.setDictCache(dictType, dictData);
            }
            return true;
        }
        return false;
    }

    /**
     * 根据id删除字典数据
     *
     * @param ids 字典数据字典编码集合
     */
    @Override
    public boolean deleteEntityById(List<Long> ids) {
        for (Long id : ids) {
            SysDictData dictData = this.getById(id);
            // 判断是否有数据
            if (StringUtils.isNull(dictData)) break;

            // 删除数据
            this.removeById(id);
            String dictType = dictData.getDictType();
            // 存入缓存
            DictUtils.setDictCache(dictType, this.listEntities(dictType, new SysDictDataQuery()));
        }
        return true;
    }

    /**
     * 根据id获取字典数据详情信息
     *
     * @param id 字典数据编码
     * @return 字典数据详情信息
     */
    @Override
    public SysDictData getEntityById(Long id) {
        return this.getById(id);
    }

    /**
     * 获取字典数值数据(字典查询)
     *
     * @param dictType 字典类型
     * @return 系统字典值数据
     */
    @Override
    public List<SysDictData> getDictDateList(String dictType) {
        // 1.从缓存中获取数据
        List<SysDictData> dictCache = DictUtils.getDictCache(dictType);

        // 2.判断缓存中是否存在，存在数据则直接返回
        if (StringUtils.isNotEmpty(dictCache)) {
            return dictCache;
        }

        // 3.数据不存在,查询数据库，再添加到缓存中
        dictCache = this.listEntities(dictType, new SysDictDataQuery());
        if (StringUtils.isNotEmpty(dictCache)) {
            // 将查询的数据存入到字典中
            DictUtils.setDictCache(dictType, dictCache);
            return dictCache;
        }

        // 4.查询不到返回空
        return null;
    }
}
