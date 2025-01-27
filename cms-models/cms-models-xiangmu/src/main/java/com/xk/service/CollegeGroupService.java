package com.xk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.common.core.web.domain.Response;
import com.xk.domain.dto.*;
import com.xk.domain.vo.group.CollegeDataVo;
import com.xk.domain.vo.group.CollegeDetailedLisVo;
import com.xk.domain.vo.group.CollegeGroupVo;
import com.xk.entity.CollegeGroup;

import javax.validation.Valid;
import java.util.List;


/**
 * 学院组表(CollegeGroup)表服务接口
 *
 * @author makejava
 * @since 2024-12-05 21:21:54
 */
public interface CollegeGroupService extends IService<CollegeGroup> {
    /**
     * 查询学院组表列表
     * @param name 学院组名称
     * @return
     */
    Response<List<collegeListDto>> collegeList(String name);

    /**
     * 根据id集查询学院组列表
     * @param ids id集
     * @return 学院组列表
     */
    List<CollegeGroup> selectByIds(List<Long> ids);

    /**
     * 根据id查询学院组
     * @param collegeGroupId 学院组id
     * @return 学院组
     */
    Response<CollegeGroupVo> selectByIdCollegeGroup(Long collegeGroupId);
    /**
     * 获取学院详细信息列表
     * @param collegeDto 学院名模糊查询
     * @return
     */
    PageDTO<CollegeDetailedLisVo> getCollegedetailedList(CollegeDto collegeDto);

    /**
     * 获取学院用户列表
     * @param id 学院id
     * @return 学院用户列表
     */
    List<CollegeDataVo> getCollegeUser(Long id);

    /**
     * 根据id修改学院组信息
     * @param updateCollegeUserDto
     * @return
     */
    boolean updateCollege( UpdateCollegeGroup updateCollegeUserDto);

    /**
     * 新增学院组
     * @param addCollegeGroup
     * @return
     */
    boolean addCollegeGroup( AddCollegeGroup addCollegeGroup);
}

