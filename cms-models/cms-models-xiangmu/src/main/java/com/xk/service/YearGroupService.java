package com.xk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.common.core.web.domain.Response;
import com.xk.domain.dto.yearListDTO;
import com.xk.entity.YearGroup;

import java.util.List;


/**
 * 年度组表(YearGroup)表服务接口
 *
 * @author makejava
 * @since 2024-12-05 22:47:31
 */
public interface YearGroupService extends IService<YearGroup> {
    /**
     * 获取年度列表
     * @return
     */
    Response<List<yearListDTO>> yearList(String name);

    /**
     * 获取当前有效的年度组,名字模糊查询
     * @param name 名称
     * @return 当前有效的年度组
     */
    List<YearGroup> getActiveYearGroups(String name);
}

