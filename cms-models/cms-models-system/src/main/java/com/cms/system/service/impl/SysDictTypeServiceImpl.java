package com.cms.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.core.utils.StringUtils;
import com.cms.common.security.utils.DictUtils;
import com.cms.system.api.domain.pojo.SysDictType;
import com.cms.system.domain.query.SysDictTypeQuery;
import com.cms.system.mapper.SysDictDataMapper;
import com.cms.system.mapper.SysDictTypeMapper;
import com.cms.system.service.SysDictTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统字典类型
 *
 * @author 邓志军
 * @date 2024年6月1日18:43:39
 */
@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements SysDictTypeService {

    @Resource
    private SysDictTypeMapper dictTypeMapper;

    @Resource
    private SysDictDataMapper dictDataMapper;

    /**
     * 查询字典类型列表数据
     *
     * @param query 查询条件
     * @return 字典类型列表
     */
    @Override
    public List<SysDictType> listEntities(SysDictTypeQuery query) {
        return this.dictTypeMapper.listEntities(query);
    }

    /**
     * 根据id获取字典类型详情信息
     *
     * @param id 字典id
     * @return 字典类型详情信息
     */
    @Override
    public SysDictType getEntityById(Long id) {
        return this.dictTypeMapper.selectById(id);
    }

    /**
     * 根据字典类型查询数据详情信息
     *
     * @param type 字典类型
     * @return 字典类型详情信息
     */
    @Override
    public SysDictType getDictTypeByDictType(String type) {
        return this.dictTypeMapper.getDictTypeByDictType(type);
    }

    /**
     * 添加字典类型数据
     *
     * @param dictType 字典类型数据
     * @return 添加结果
     */
    @Override
    public boolean addEntity(SysDictType dictType) {
        // 1.查询判断是否数据已存在
        if (!StringUtils.isNull(this.getDictTypeByDictType(dictType.getDictType()))) {
            throw new ServiceException(String.format("%s类型字典已存在，不允许重复添加!", dictType.getDictType()));
        }
        // 2.添加数据
        return this.save(dictType);
    }

    /**
     * 根据id删除字典类型数据
     *
     * @param ids 根据id删除字典类型数据
     */
    @Override
    public boolean deleteEntityById(List<Long> ids) {
        // 1.遍历拿到所有的id
        for (Long id : ids) {
            // 2.查询字典详情数据
            SysDictType dictType = this.getById(id);
            // 3.查询字典类型数据是否有数据
            if (this.dictDataMapper.getDictDataCount(dictType.getDictType()) > 0) {
                throw new ServiceException(String.format("%s已分配，不允许删除!", dictType.getDictName()));
            }
            // 删除数据
            this.removeById(id);
            // 删除缓存
            DictUtils.removeDictCache(dictType.getDictType());
        }
        return true;
    }

    /**
     * 修改字典类型信息
     *
     * @param dictType 字典类型信息
     */
    @Override
    public int updateEntity(SysDictType dictType) {
        // 1.查询原来的数据
        SysDictType old_dict = this.getById(dictType.getDictId());

        // 2.判断字典类型是否进行修改了，并且数据库中没有该类型的字典
        if(!dictType.getDictType().equals(old_dict.getDictType()) && StringUtils.isNull(this.dictTypeMapper.getDictTypeByDictType(dictType.getDictType()))) {
            // 修改字典下的数据类型
            this.dictDataMapper.updateDictDateType(old_dict.getDictType(),dictType.getDictType());
            // 删除缓存数据
            DictUtils.removeDictCache(dictType.getDictType());
        }

        // 3.修改字典类型数据
        return this.dictTypeMapper.updateById(dictType);
    }
}
