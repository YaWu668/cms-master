package com.cms.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.constant.UserConstants;
import com.cms.common.core.utils.DateUtils;
import com.cms.common.core.utils.StringUtils;
import com.cms.common.core.web.page.PageParams;
import com.cms.system.api.domain.pojo.SysLogininfor;
import com.cms.system.domain.query.SysLogininforQuery;
import com.cms.system.mapper.SysLogininforMapper;
import com.cms.system.service.SysLogininforService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统访问记录 Service 实现
 *
 * @author 邓志军
 * @date 2024-05-29
 */
@Service
public class SysLogininforServiceImpl extends ServiceImpl<SysLogininforMapper, SysLogininfor> implements SysLogininforService {

    @Resource
    private SysLogininforMapper sysLogininforMapper;

    /**
     * 查询系统访问记录列表数据(无分页)
     *
     * @param sysLogininfor 系统访问记录
     * @return 系统访问记录列表数据
     */
    @Override
    public List<SysLogininfor> listAllEntities(SysLogininfor sysLogininfor) {
        // 1.构建查询条件对象
        LambdaQueryWrapper<SysLogininfor> queryWrapper = new LambdaQueryWrapper<>();

        // 2.返回数据
        return this.sysLogininforMapper.selectList(queryWrapper);
    }

    /**
     * 查询系统访问记录列表分页数据
     *
     * @param query      系统访问记录
     * @param pageParams 分页查询参数
     * @return 系统访问记录列表数据
     */
    @Override
    public IPage<SysLogininfor> listEntities(SysLogininforQuery query, PageParams pageParams) {
        // 1.构建分页对象
        IPage<SysLogininfor> page = new Page<>(pageParams.getCurrent(), pageParams.getLimit());

        // 2.构建查询条件对象
        LambdaQueryWrapper<SysLogininfor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(query.getUserName()), SysLogininfor::getUserName, query.getUserName());
        queryWrapper.eq(StringUtils.isNotEmpty(query.getStatus()), SysLogininfor::getStatus, query.getStatus());
        if (query.getBeginTime() != null && query.getEndTime() != null) {
            queryWrapper.between(SysLogininfor::getAccessTime,
                    DateUtils.formatIntradayMinDateTime(query.getBeginTime()),
                    DateUtils.formatIntradayMaxDateTime(query.getEndTime())
            );
        }
        queryWrapper.orderByDesc(SysLogininfor::getAccessTime);
        queryWrapper.eq(SysLogininfor::getType, UserConstants.LOGIN);

        // 3.执行分页查询
        this.sysLogininforMapper.selectPage(page, queryWrapper);

        // 4.返回数据
        return page;
    }

    /**
     * 根据id查询系统访问记录详细信息
     *
     * @param id 系统访问记录表数据id
     */
    @Override
    public SysLogininfor getEntityById(Long id) {
        return this.getById(id);
    }

    /**
     * 添加系统访问记录数据
     *
     * @param sysLogininfor 系统访问记录
     * @return 添加系统访问记录数据成功返回 true 否则返回 false
     */
    @Override
    public boolean addEntity(SysLogininfor sysLogininfor) {
        sysLogininfor.setAccessTime(DateUtils.getNowDate());
        return this.save(sysLogininfor);
    }
}
