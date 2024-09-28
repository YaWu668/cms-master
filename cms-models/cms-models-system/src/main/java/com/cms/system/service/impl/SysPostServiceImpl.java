package com.cms.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.core.utils.StringUtils;
import com.cms.system.domain.pojo.SysPost;
import com.cms.system.domain.query.SysPostListQuery;
import com.cms.system.domain.vo.SysPostListVo;
import com.cms.system.domain.vo.TreeSelect;
import com.cms.system.mapper.SysPostMapper;
import com.cms.system.service.SysPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统岗位
 *
 * @author 邓志军
 * @date 2024年6月1日13:07:23
 */
@Service
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements SysPostService {

    @Resource
    private SysPostMapper postMapper;

    /**
     * 查询岗位信息列表数据
     *
     * @param query 岗位查询信息
     * @return 钢围堰信息列表
     */
    @Override
    public List<SysPostListVo> listAllEntities(SysPostListQuery query) {
        return this.postMapper.listAllEntities(query);
    }

    /**
     * 获取岗位树列表
     */
    @Override
    public List<TreeSelect> selectDeptTreeList() {
        // 1.获取列表数据
        List<SysPost> list = this.list();
        // 2.返回数据
        return list.stream().map(post -> TreeSelect.builder()
                        .label(post.getPostName())
                        .value(post.getPostId())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 添加岗位信息
     *
     * @param post 岗位信息
     */
    @Override
    public boolean addEntity(SysPost post) {
        // 1.判断岗位编码是否存在
        if (!this.checkPostCodeUnique(post.getPostCode())) {
            throw new ServiceException(String.format("岗位添加失败，岗位编码：%s 已存在!!!", post.getPostCode()));
        }
        // 2.判断岗位名称是否存在
        if (!this.checkPostNameUnique(post.getPostName())) {
            throw new ServiceException(String.format("岗位添加失败，岗位名称：%s 已存在!!!", post.getPostName()));
        }
        // 3.保存岗位信息
        return this.save(post);
    }

    /**
     * 修改岗位信息
     *
     * @param post 岗位信息
     */
    @Override
    public boolean updateEntity(SysPost post) {
        // 1.查询原始的数据
        SysPost oldPost = this.getEntityById(post.getPostId());
        // 2.判断岗位名称是否存在
        if (!oldPost.getPostName().equals(post.getPostName()) && !this.checkPostNameUnique(post.getPostName())) {
            throw new ServiceException(String.format("岗位修改失败，岗位编码：%s 已存在!!!", post.getPostCode()));
        }
        // 3.判断岗位编码是否存在
        if (!oldPost.getPostName().equals(post.getPostName()) && !this.checkPostCodeUnique(post.getPostCode())) {
            throw new ServiceException(String.format("岗位修改失败，岗位名称：%s 已存在!!!", post.getPostName()));
        }
        // 4.修改岗位信息
        return this.updateById(post);
    }

    /**
     * 查询岗位详情信息
     *
     * @param id 岗位id
     * @return 岗位详情信息
     */
    @Override
    public SysPost getEntityById(Long id) {
        return this.getById(id);
    }

    /**
     * 根据id删岗位信息
     *
     * @param ids 岗位id集合
     */
    @Override
    public boolean deleteEntity(List<Long> ids) {
        for (Long id : ids) {
            // 1.判断该岗位下是否有用户
            if (this.postMapper.countUserPostByPostId(id) > 0) {
                throw new ServiceException(String.format("岗位编号：%s 有绑定用户，无法删除!!!", id));
            }
            // 2.删除数据
            this.removeById(id);
        }
        return true;
    }

    /**
     * 根据岗位编码判断岗位是否存在
     *
     * @param code 岗位编码
     */
    @Override
    public boolean checkPostCodeUnique(String code) {
        SysPost sysPost = this.postMapper.checkPostCodeUnique(code);
        return StringUtils.isNull(sysPost);
    }

    /**
     * 根据岗位名称判断岗位是否存在
     *
     * @param name 岗位名称
     */
    @Override
    public boolean checkPostNameUnique(String name) {
        SysPost sysPost = this.postMapper.checkPostNameUnique(name);
        return StringUtils.isNull(sysPost);
    }
}
