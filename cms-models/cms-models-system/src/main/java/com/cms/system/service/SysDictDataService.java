package com.cms.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.system.api.domain.pojo.SysDictData;
import com.cms.system.domain.query.SysDictDataQuery;

import java.util.List;

/**
 * 系统字典数据
 *
 * @author 邓志军
 * @date 2024年6月1日18:32:50
 */
public interface SysDictDataService extends IService<SysDictData> {

    /**
     * 获取系统字典值数据列表
     *
     * @param dictType 字典类型
     * @param query    查询条件
     * @return 系统字典值数据列表
     */
    List<SysDictData> listEntities(String dictType,SysDictDataQuery query);

    /**
     * 添加系统字典数据
     *
     * @param sysDictData 字典数据详情
     */
    boolean addEntity(SysDictData sysDictData);

    /**
     * 修改系统字典数据
     *
     * @param sysDictData 字典数据详情
     */
    boolean updateEntity(SysDictData sysDictData);

    /**
     * 根据id删除字典数据
     *
     * @param ids 字典数据字典编码集合
     */
    boolean deleteEntityById(List<Long> ids);

    /**
     * 根据id获取字典数据详情信息
     *
     * @param id 字典数据编码
     * @return 字典数据详情信息
     */
    SysDictData getEntityById(Long id);

    /**
     * 获取字典数值数据(字典查询)
     *
     * @param dictType 字典类型
     * @return 系统字典值数据
     */
    List<SysDictData> getDictDateList(String dictType);
}
