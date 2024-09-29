package com.cms.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cms.system.domain.pojo.SysConfig;
import com.cms.system.domain.query.SysConfigQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统参数设置
 *
 * @author 邓志军
 * @date 2024年8月15日15:15:23
 */
@Mapper
public interface SysConfigMapper extends BaseMapper<SysConfig> {

    /**
     * 查询系统参数设置列表
     *
     * @param configQuery 参数查询条件
     * @return 系统参数设置列表数据
     */
    List<SysConfig> getEntityList(SysConfigQuery configQuery);

    /**
     * 删除系统配置详情信息
     *
     * @param ids 系统配置id
     */
    void deleteEntity(@Param("ids") List<Long> ids);
}
