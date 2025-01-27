package com.xk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xk.domain.dto.AddCollegeUserDto;
import com.xk.entity.CollegeData;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * 学院组人员表(CollegeData)表服务接口
 *
 * @author YaWu
 * @since 2024-12-19 00:56:46
 */
public interface CollegeDataService extends IService<CollegeData> {
    /**
     * 获取当前用户的学院组id集合
     * @return 学院组id集合
     */
    List<Long> getCollegeIdByUserId();

    /**
     * 学院id添加人员
     * @param addCollegeUserDto
     * @return
     */
    boolean addCollegeUser(AddCollegeUserDto addCollegeUserDto);

    /**
     * 根据用户id添加学院角色,如果用户已经有学院角色则不添加,如果用户没有学院角色则添加
     * @param userId 用户id
     * @return
     */
    void addCollegeRole(Long userId);

    /**
     * 删除学院用户,如果学院用户有数据则不删除,如果学院用户没有数据则删除<br>
     * 该方法不可以抛出异常,否者删除学院组组人员会失败,因为会出现,在当前组内,但是又没有角色,这样就会出现异常,导致删除失败
     * @param userId 用户id
     */
    void deleteColleRole(Long userId);

    /**
     * 根据学院id和用户id删除学院人员
     * @param collegeGroupId 学院
     * @param userId 用户
     * @return
     */
    boolean deleteCollegeUser(Long id);
}

