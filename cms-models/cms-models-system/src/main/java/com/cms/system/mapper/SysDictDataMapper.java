package com.cms.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cms.system.api.domain.pojo.SysDictData;
import com.cms.system.domain.query.SysDictDataQuery;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 系统字典数据
 */
public interface SysDictDataMapper extends BaseMapper<SysDictData> {

    /**
     * 获取系统字典值数据列表
     *
     * @param dictType 字典类型
     * @param query    查询条件
     * @return 系统字典值数据列表
     */
    List<SysDictData> listEntities(@Param("dictType") String dictType, @Param("query") SysDictDataQuery query);

    /**
     * 查询字典类型数据有多少条
     *
     * @param dictType 字典类型
     * @return 数量
     */
    @Select("select count(1) from sys_dict_data where dict_type = #{dictType}")
    int getDictDataCount(String dictType);

    /**
     * 修改字典类型
     *
     * @param oldType 老的字典类型
     * @param newType 新的字典类型
     */
    @Update("update sys_dict_data set dict_type = #{newType} where dict_type = #{oldType}")
    boolean updateDictDateType(@Param("oldType") String oldType, @Param("newType") String newType);
}
