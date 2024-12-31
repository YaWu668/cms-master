package com.xk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.common.core.web.domain.Response;
import com.xk.domain.dto.*;
import com.xk.domain.vo.group.YearDetailedLisVo;
import com.xk.domain.vo.group.YearGroupVo;
import com.xk.entity.YearGroup;

import javax.validation.Valid;
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


    /**
     * 根据年度组id集合获取年度集合
     * @param ids 年度组id
     * @return
     */
    List<YearGroup> selectBatchyearGroupIds(List<Long> ids);
    /**
     * 根据id查询年度组
     * @param yearGroupId 年度组id
     * @return 年度组vo类
     */
    Response<YearGroupVo> selectByIdYearGroup(Long yearGroupId);
    /**
     * 查询年度详细列表
     * @param yearDetailedListDto 查询条件
     * @return 全部年度组的详细数据
     */
    PageDTO<YearDetailedLisVo> getYearDetailedList(YearDetailedListDto yearDetailedListDto);
    /**
     * 添加年度组
     * @param addYearGroup 年度组
     * @return
     */
    Response addYearGroup(@Valid AddYearGroup addYearGroup);
    /**
     * 根据年度组Id进行修改后年度组,年度组禁用的话,<br>学生申请项目无法绑定禁用的
     */
    Response updateYearGroup(@Valid UpdateYearGroup updateYearGroup);
}

