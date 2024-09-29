package com.cms.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cms.system.api.domain.dto.SysDeptDto;
import com.cms.system.api.domain.pojo.SysDept;
import com.cms.system.domain.query.SysDeptQuery;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 部门管理 数据层
 *
 * @author 邓志军
 * @date 2024年5月30日19:27:05
 */
public interface SysDeptMapper extends BaseMapper<SysDept> {

    @Select("select * from sys_dept where del_flag = '0'")
    List<SysDeptDto> listAllEntities();

    /**
     * 获取部门列表信息
     *
     * @param query 查询条件参数
     * @return 部门列表信息
     */
    List<SysDeptDto> listEntities(SysDeptQuery query);

    /**
     * 校验部门名称是否唯一
     *
     * @param deptName 部门名称
     * @param parentId 父部门ID
     * @return 结果
     */
    @Select("select * from sys_dept where dept_name = #{deptName} and parent_id = #{parentId} limit 1")
    SysDept checkDeptNameUnique(@Param("deptName") String deptName, @Param("parentId") Long parentId);

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    @Select("select count(*) from sys_dept where status = 0 and del_flag = '0' and find_in_set(#{deptId}, ancestors)")
    int selectNormalChildrenDeptById(Long deptId);

    /**
     * 根据部门id查询所有子部门
     *
     * @param deptId 部门id
     * @return 子部门数据
     */
    List<SysDept> selectChildrenDeptById(Long deptId);

    /**
     * 修改子元素关系
     *
     * @param depts 子元素
     * @return 结果
     */
    int updateDeptChildren(@Param("depts") List<SysDept> depts);

    /**
     * 修改所在部门正常状态
     *
     * @param deptIds 部门ID组
     */
    void updateDeptStatusNormal(Long[] deptIds);

    /**
     * 判断部门下是否存在子部门
     *
     * @param deptId 部门id
     */
    @Select("select count(1) from sys_dept where del_flag = '0' and parent_id = #{deptId};")
    int hasChildByDeptId(Long deptId);

    /**
     * 判断部门下是否存在绑定的用户
     *
     * @param deptId 部门id
     */
    @Select("select count(1) from sys_user where del_flag = '0' and dept_id = #{deptId};")
    int checkDeptExistUser(Long deptId);
}
