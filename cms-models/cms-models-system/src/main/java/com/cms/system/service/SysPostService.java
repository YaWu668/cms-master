package com.cms.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.system.domain.pojo.SysPost;
import com.cms.system.domain.query.SysPostListQuery;
import com.cms.system.domain.vo.SysPostListVo;
import com.cms.system.domain.vo.TreeSelect;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统岗位
 *
 * @author 邓志军
 * @date 2024年6月1日13:06:40
 */
public interface SysPostService extends IService<SysPost> {

    /**
     * 查询岗位信息列表数据
     *
     * @param query 岗位查询信息
     * @return 钢围堰信息列表
     */
    List<SysPostListVo> listAllEntities(SysPostListQuery query);

    /**
     * 获取岗位树列表
     */
    List<TreeSelect> selectDeptTreeList();

    /**
     * 添加岗位信息
     *
     * @param post 岗位信息
     */
    boolean addEntity(SysPost post);

    /**
     * 修改岗位信息
     *
     * @param post 岗位信息
     */
    boolean updateEntity(SysPost post);

    /**
     * 查询岗位详情信息
     *
     * @param id 岗位id
     * @return 岗位详情信息
     */
    SysPost getEntityById(Long id);

    /**
     * 根据id删岗位信息
     *
     * @param ids 岗位id集合
     */
    @Transactional
    boolean deleteEntity(List<Long> ids);

    /**
     * 根据岗位编码判断岗位是否存在
     *
     * @param code 岗位编码
     */
    boolean checkPostCodeUnique(String code);

    /**
     * 根据岗位名称判断岗位是否存在
     *
     * @param name 岗位名称
     */
    boolean checkPostNameUnique(String name);
}
