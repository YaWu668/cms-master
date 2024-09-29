package com.cms.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.system.api.domain.pojo.SysDictType;
import com.cms.system.domain.query.SysDictTypeQuery;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统字典类型
 */
public interface SysDictTypeService extends IService<SysDictType> {

    /**
     * 查询字典类型列表数据
     *
     * @param query 查询条件
     * @return 字典类型列表
     */
    List<SysDictType> listEntities(SysDictTypeQuery query);

    /**
     * 根据id获取字典类型详情信息
     *
     * @param id 字典id
     * @return 字典类型详情信息
     */
    SysDictType getEntityById(Long id);

    /**
     * 根据字典类型查询数据详情信息
     *
     * @param type 字典类型
     * @return 字典类型详情信息
     */
    SysDictType getDictTypeByDictType(String type);

    /**
     * 添加字典类型数据
     *
     * @param dictType 字典类型数据
     * @return 添加结果
     */
    boolean addEntity(SysDictType dictType);

    /**
     * 根据id删除字典类型数据
     *
     * @param ids 根据id删除字典类型数据
     */
    @Transactional
    boolean deleteEntityById(List<Long> ids);

    /**
     * 修改字典类型信息
     *
     * @param dictType 字典类型信息
     */
    @Transactional
    int updateEntity(SysDictType dictType);
}
