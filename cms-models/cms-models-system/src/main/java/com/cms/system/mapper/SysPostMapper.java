package com.cms.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cms.system.api.domain.pojo.SysDept;
import com.cms.system.domain.pojo.SysPost;
import com.cms.system.domain.query.SysPostListQuery;
import com.cms.system.domain.vo.SysPostListVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统岗位Mapper
 *
 * @author 邓志军
 * @date 2024-05-29
 */
public interface SysPostMapper extends BaseMapper<SysPost> {

    /**
     * 查询岗位信息列表数据
     *
     * @param query 岗位查询信息
     * @return 钢围堰信息列表
     */
    List<SysPostListVo> listAllEntities(SysPostListQuery query);

    /**
     * 根据岗位id查询，该角色下绑定了多少个用户
     *
     * @param postId 岗位id
     */
    @Select("select count(1) from sys_user_post where post_id = #{postId}")
    Integer countUserPostByPostId(Long postId);

    /**
     * 根据岗位编码判断岗位是否存在
     *
     * @param code 岗位编码
     */
    @Select("select * from sys_user_post where post_code = #{code}")
    SysPost checkPostCodeUnique(String code);

    /**
     * 根据岗位名称判断岗位是否存在
     *
     * @param name 岗位名称
     */
    @Select("select * from sys_user_post where post_name = #{name}")
    SysPost checkPostNameUnique(String name);

    /**
     * 绑定用户和岗位关系
     *
     * @param userId 用户id
     * @param postId 角色id
     * @return
     */
    @Insert("insert into sys_user_post value(#{userId}, #{postId})")
    int buildUserPost(@Param("userId") Long userId, @Param("postId") Long postId);

    /**
     * 取消用户在该岗位下的所有绑定
     *
     * @param userId 用户id
     */
    @Delete("delete from sys_user_post where user_id = #{userId}")
    int cancelUserBuildPost(Long userId);
}
