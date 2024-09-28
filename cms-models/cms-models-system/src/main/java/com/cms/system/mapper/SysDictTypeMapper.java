package com.cms.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cms.system.api.domain.pojo.SysDictType;
import com.cms.system.domain.query.SysDictTypeQuery;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统字典类型
 */
public interface SysDictTypeMapper extends BaseMapper<SysDictType> {

    /**
     * 查询字典类型列表数据
     *
     * @param query 查询条件
     * @return 字典类型列表
     */
    List<SysDictType> listEntities(SysDictTypeQuery query);

    /**
     * 根据字典类型id查询详情信息
     *
     * @param id
     * @return
     */
    @Select("select * from sys_dict_type where dict_id = #{sys_dict_type}")
    SysDictType selectById(Long id);


    /**
     * 根据字典类型查询数据详情信息
     *
     * @param type 字典类型
     * @return 字典类型详情信息
     */
    @Select("select * from sys_dict_type where dict_type = #{dictType} ")
    SysDictType getDictTypeByDictType(String type);

}
