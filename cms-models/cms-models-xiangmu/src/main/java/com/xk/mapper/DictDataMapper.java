package com.xk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cms.system.api.domain.pojo.SysDictData;
import com.xk.entity.DictData;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


/**
 * 字典数据表(DictData)表数据库访问层
 *
 * @author makejava
 * @since 2024-12-06 00:03:02
 */
public interface DictDataMapper extends BaseMapper<DictData> {
    @Select("<script>"
            + "SELECT d.* FROM sys_dict_data d "
            + "WHERE d.dict_type IN "
            + "  <foreach collection='types' item='type' open='(' separator=',' close=')'>"
            + "    #{type}"
            + "  </foreach> "
            + "ORDER BY d.dict_type, d.dict_sort"
            + "</script>")
    List<DictData> selectByDictTypes(@Param("types") List<String> types);
}

