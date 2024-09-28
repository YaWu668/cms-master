package com.cms.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.constant.UserConstants;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.core.text.Convert;
import com.cms.common.core.utils.StringUtils;
import com.cms.system.api.domain.dto.SysDeptDto;
import com.cms.system.api.domain.pojo.SysDept;
import com.cms.system.domain.query.SysDeptQuery;
import com.cms.system.domain.vo.TreeSelect;
import com.cms.system.mapper.SysDeptMapper;
import com.cms.system.service.SysDeptService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 部门管理 数据层
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    @Resource
    private SysDeptMapper sysDeptMapper;

    /**
     * 查询部门信息列表数据(无分页)
     *
     * @param dept 部门信息
     * @return 部门信息列表
     */
    @Override
    public List<SysDeptDto> listAllEntities(SysDeptDto dept) {
        return this.sysDeptMapper.listAllEntities();
    }

    /**
     * 查询部门树结构信息
     *
     * @param dept 部门信息
     * @return 部门树信息集合
     */
    @Override
    public List<TreeSelect> selectDeptTreeList(SysDeptDto dept) {
        List<SysDeptDto> depts = this.listAllEntities(dept);
        return this.buildDeptTreeSelect(depts);
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildDeptTreeSelect(List<SysDeptDto> depts) {
        // 1.获取部门列表树
        List<SysDeptDto> deptTrees = this.buildDeptTree(depts);
        // 2.组装前端所需列表树
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 构建前端所需要树结构，组装一级节点
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    public List<SysDeptDto> buildDeptTree(List<SysDeptDto> depts) {
        List<SysDeptDto> deptList = new ArrayList<>();
        // 1.拿到所有的部门id
        List<Long> dept_ids = depts.stream().map(SysDept::getDeptId).collect(Collectors.toList());
        // 2.遍历所有的部门
        for (SysDeptDto dept : depts) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!dept_ids.contains(dept.getParentId())) {
                // 3.递归组装子节点
                this.recursionFn(depts, dept);
                // 4.将父元素存入数组
                deptList.add(dept);
            }
        }
        // 5.返回数据
        return depts.isEmpty() ? depts : deptList;
    }

    /**
     * 获取部门列表信息
     *
     * @param query 查询条件参数
     * @return 部门列表信息
     */
    @Override
    public List<SysDeptDto> listEntities(SysDeptQuery query) {
        List<SysDeptDto> depts = this.sysDeptMapper.listEntities(query);
        return this.buildDeptTree(depts);
    }

    /**
     * 添加部门信息
     *
     * @param dept 部门信息
     */
    @Override
    public boolean addEntity(SysDept dept) {
        // 1.判断父节点是否已经停用，如果停用不允许添加。
        SysDept parentDept = this.getEntityById(dept.getParentId());
        if (!UserConstants.DEPT_NORMAL.equals(parentDept.getStatus())) {
            throw new ServiceException("部门停用，不允许新增");
        }

        // 2.判断部门名称是否已经重复, 在一个结点下不允许出现二个相同的部门
        if (this.checkDeptNameUnique(dept)) {
            throw new ServiceException(String.format("新增部门 %s 失败，该部门已存在!!!", dept.getDeptName()));
        }
        // 3.拼接祖籍节点
        dept.setAncestors(parentDept.getAncestors() + "," + parentDept.getDeptId());

        // 4.添加数据
        return this.save(dept);
    }

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return true 如果部门名称在指定的父级部门下是唯一的，否则返回 false
     */
    @Override
    public boolean checkDeptNameUnique(SysDept dept) {
        // 1.获取部门id，如果没有id，说明是新增给-1
        long deptId = StringUtils.isNull(dept.getDeptId()) ? -1L : dept.getDeptId();
        // 2.根据部门名称以及父级部门id查询数据
        SysDept currentDept = this.sysDeptMapper.checkDeptNameUnique(dept.getDeptName(), dept.getParentId());
        // 3.如果，查询的部门不是空的，且传递的部门id == 查询出来的id则说明数据存在
        return StringUtils.isNotNull(currentDept) && deptId == currentDept.getDeptId();
    }

    /**
     * 修改部门信息
     *
     * @param dept 部门信息
     */
    @Override
    public boolean updateEntity(SysDept dept) {
        Long deptId = dept.getDeptId();

        // 1.判断父节点id是否是自己，如果是自己抛出异常。
        if (dept.getParentId().equals(deptId)) {
            throw new ServiceException(String.format("修改部门 %s 失败，父级部门不允许是自己!!!", dept.getDeptName()));
        }

        // 2.判断修改的节点是否已经存在，如果存在抛出异常
        if (this.checkDeptNameUnique(dept)) {
            throw new ServiceException(String.format("修改部门 %s 失败，该部门已存在!!!", dept.getDeptName()));
        }

        // 3.当前结点如果是停用状态，那么子节点下的所有部门都必须停用，否则抛出异常
        if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus()) && this.selectNormalChildrenDeptById(deptId) > 0) {
            throw new ServiceException("该部门包含未停用的子部门！");
        }

        // 4.新的父节点数据不允许为空，且老的数据也不可以为空，并且新的父级id和老的父级id不是同一个，则修改祖级列
        SysDept newParentDept = this.getEntityById(dept.getParentId()); // 新的父级部门
        SysDept oldCurrentDept = this.getEntityById(deptId); // 修改前的部门
        if (StringUtils.isNotNull(newParentDept) // 新的父部门不是空的
                && StringUtils.isNotNull(oldCurrentDept) // 老的当前部门不是空的
                && !Objects.equals(this.getEntityById(oldCurrentDept.getParentId()).getDeptId(), dept.getParentId()) // 老的父级id 和 新的父级id不相同
        ) {
            // 祖级id = 父级部门的祖级id + 父级部门id
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getDeptId();
            String oldAncestors = dept.getAncestors();
            dept.setAncestors(newAncestors);

            // 修改所有的子部门，的祖级id
            this.updateDeptChildren(deptId, newAncestors, oldAncestors);
        }

        // 5.当前结点是启用状态，当前结点的历史节点不为空，并且上级部门不是0则设置该部门上的所有部门为启用
        if (UserConstants.DEPT_NORMAL.equals(dept.getStatus()) && StringUtils.isNotEmpty(dept.getAncestors()) && !StringUtils.equals("0", dept.getAncestors())) {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            this.updateParentDeptStatusNormal(dept);
        }

        // 6.修改当前部门数据
        return this.updateById(dept);
    }

    /**
     * 修改该部门的父级部门状态
     *
     * @param dept 当前部门
     */
    private void updateParentDeptStatusNormal(SysDept dept) {
        // 获取部门的祖籍部门列表
        String ancestors = dept.getAncestors();
        // 转换为数组
        Long[] deptIds = Convert.toLongArray(ancestors);
        // 修改部门状态
        this.sysDeptMapper.updateDeptStatusNormal(deptIds);
    }

    /**
     * 修改子元素关系
     *
     * @param deptId       被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
        // 1.查询所有的子部门
        List<SysDept> children = this.sysDeptMapper.selectChildrenDeptById(deptId);

        // 2.遍历子部门列表，更新每个子部门的祖先路径信息
        children.forEach(child -> child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors)));

        // 3.如果子元素不是空的，则开始批量修改元素
        if (children.size() > 0) {
            this.sysDeptMapper.updateDeptChildren(children);
        }
    }

    /**
     * 根据id获取部门详情信息
     *
     * @param deptId 部门id
     * @return 部门详细信息
     */
    @Override
    public SysDept getEntityById(Long deptId) {
        return getById(deptId);
    }

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    @Override
    public int selectNormalChildrenDeptById(Long deptId) {
        return this.sysDeptMapper.selectNormalChildrenDeptById(deptId);
    }

    /**
     * 根据id删除部门信息
     *
     * @param id 部门id
     */
    @Override
    public boolean deleteEntityByDeptId(Long id) {
        // 1.部门存在子部门不允许删除
        if (this.hasChildByDeptId(id)) {
            throw new ServiceException("删除部门失败，该部门下存在子部门!!!");
        }

        // 2.部门存在绑定人员不允许删除
        if (this.checkDeptExistUser(id)) {
            throw new ServiceException("删除部门失败，该部门下存在绑定用户!!!");
        }

        // 3.删除部门
        return this.removeById(id);
    }

    /**
     * 判断部门下是否存在子部门
     *
     * @param deptId 部门id
     * @return 存在部门返回 true
     */
    private boolean hasChildByDeptId(Long deptId) {
        return this.sysDeptMapper.hasChildByDeptId(deptId) > 0;
    }

    /**
     * 判断部门下是否存在用户
     *
     * @param deptId 部门id
     * @return 存在部门返回 true
     */
    private boolean checkDeptExistUser(Long deptId) {
        return this.sysDeptMapper.checkDeptExistUser(deptId) > 0;
    }

    /**
     * 递归列表,组装子元素
     *
     * @param depts  所有的部门数据
     * @param parent 父节点
     */
    private void recursionFn(List<SysDeptDto> depts, SysDeptDto parent) {
        // 1.获取子节点
        List<SysDeptDto> childList = this.getChildList(depts, parent);
        parent.setChildren(childList);
        // 2.遍历所有子节点，判断是否还有子节点
        for (SysDeptDto dept : childList) {
            // 判断还有没有子节点
            if (hasChild(depts, dept)) {
                // 3.递归继续组装数据
                this.recursionFn(depts, dept);
            }
        }
    }

    /**
     * 组装子节点列表，
     *
     * @param depts  所有的部门数据
     * @param parent 父节点
     */
    private List<SysDeptDto> getChildList(List<SysDeptDto> depts, SysDeptDto parent) {
        // 1.新建数组，存放找到的下级节点
        List<SysDeptDto> sysDeptDtos = new ArrayList<>();

        // 2.遍历所有节点，寻找子节点
        for (SysDeptDto dept : depts) {
            // 元素的父元素不能是空，并且 元素的父节点 = 传递的节点id
            if (StringUtils.isNotNull(dept.getParentId()) && Objects.equals(parent.getDeptId(), dept.getParentId())) {
                sysDeptDtos.add(dept);
            }
        }
        return sysDeptDtos;
    }

    /**
     * 判断是否有子节点，
     *
     * @param depts  所有的部门数据
     * @param parent 父节点
     */
    private boolean hasChild(List<SysDeptDto> depts, SysDeptDto parent) {
        return getChildList(depts, parent).size() > 0;
    }
}
