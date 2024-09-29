package com.cms.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.common.core.web.page.PageParams;
import com.cms.system.api.domain.pojo.SysLogininfor;
import com.cms.system.domain.query.SysLogininforQuery;

import java.util.List;

/**
 * 系统访问记录 Service 接口
 *
 * @author 邓志军
 * @date 2024-05-29
 */
public interface SysLogininforService extends IService<SysLogininfor> {

    /**
     * 查询系统访问记录列表数据(无分页)
     *
     * @param sysLogininfor 系统访问记录
     * @return 系统访问记录列表数据
     */
    List<SysLogininfor> listAllEntities(SysLogininfor sysLogininfor);

    /**
     * 查询系统访问记录列表分页数据
     *
     * @param query 系统访问记录
     * @param pageParams 分页查询参数
     * @return 系统访问记录列表数据
     */
    IPage<SysLogininfor> listEntities(SysLogininforQuery query, PageParams pageParams);

    /**
     * 根据id查询系统访问记录详细信息
     *
     * @param id 系统访问记录表数据id
     */
    SysLogininfor getEntityById(Long id);

    /**
     * 添加系统访问记录数据
     *
     * @param sysLogininfor 系统访问记录
     * @return 添加系统访问记录数据成功返回 true 否则返回 false
     */
    boolean addEntity(SysLogininfor sysLogininfor);
}
