package com.cms.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.system.api.domain.dto.SysDeptDto;
import com.cms.system.api.domain.pojo.SysDept;
import com.cms.system.domain.query.SysDeptQuery;
import com.cms.system.domain.vo.TreeSelect;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 部门管理 服务层
 *
 * @author 邓志军
 * @date 2024年5月30日19:27:49
 */
public interface SysDeptService extends IService<SysDept> {

    /**
     * 查询部门信息列表数据(无分页)
     *
     * @param dept 部门信息
     * @return 部门信息列表
     */
    List<SysDeptDto> listAllEntities(SysDeptDto dept);

    /**
     * 查询部门树结构信息
     *
     * @param dept 部门信息
     * @return 部门树信息集合
     */
    List<TreeSelect> selectDeptTreeList(SysDeptDto dept);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    List<TreeSelect> buildDeptTreeSelect(List<SysDeptDto> depts);

    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    List<SysDeptDto> buildDeptTree(List<SysDeptDto> depts);

    /**
     * 获取部门列表信息
     *
     * @param query 查询条件参数
     * @return 部门列表信息
     */
    List<SysDeptDto> listEntities(SysDeptQuery query);

    /**
     * 添加部门信息
     *
     * @param dept 部门信息
     */
    boolean addEntity(SysDept dept);

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return true 如果部门名称在指定的父级部门下是唯一的，否则返回 false
     */
    boolean checkDeptNameUnique(SysDept dept);

    /**
     * 修改部门信息
     *
     * @param dept 部门信息
     */
    @Transactional
    boolean updateEntity(SysDept dept);

    /**
     * 根据id获取部门详情信息
     *
     * @param deptId 部门id
     * @return 部门详细信息
     */
    SysDept getEntityById(Long deptId);

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    int selectNormalChildrenDeptById(Long deptId);

    /**
     * 根据id删除部门信息
     *
     * @param id 部门id
     */
    boolean deleteEntityByDeptId(Long id);
}
