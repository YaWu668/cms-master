package com.xk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.security.utils.SecurityUtils;
import com.xk.constant.RoleConstant;
import com.xk.domain.dto.AddCollegeUserDto;
import com.xk.entity.CollegeData;
import com.xk.entity.UserRole;
import com.xk.mapper.CollegeDataMapper;
import com.xk.mapper.RoleMapper;
import com.xk.mapper.UserRoleMapper;
import com.xk.service.CollegeDataService;
import com.xk.utils.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 学院组人员表(CollegeData)表服务实现类
 *
 * @author YaWu
 * @since 2024-12-19 00:56:46
 */
@Service("collegeDataService")
@RequiredArgsConstructor
public class CollegeDataServiceImpl extends ServiceImpl<CollegeDataMapper, CollegeData> implements CollegeDataService {
    /**
     * 角色mapper
     */
    private final RoleMapper roleMapper;
    /**
     * 用户角色mapper
     */
    private final UserRoleMapper userRoleMapper;
    /**
     * 学院数据mapper
     */
    private final CollegeDataMapper collegeDataMapper;
    @Override
    public List<Long> getCollegeIdByUserId() {
        //1.获取用户id
        Long userId = SecurityUtils.getUserId();

        //2.构造条件
        return this.lambdaQuery()
                .eq(CollegeData::getUserId, userId)
                .list()
                .stream()
                .map(CollegeData::getCollegeGroupId)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public boolean addCollegeUser(AddCollegeUserDto addCollegeUserDto) {
        //1.判断是否已经存在学院和人员
        List<CollegeData> list = this.lambdaQuery()
                .eq(CollegeData::getCollegeGroupId, addCollegeUserDto.getCollegeGroupId())
                .eq(CollegeData::getUserId, addCollegeUserDto.getUserId())
                .list();
        if (!list.isEmpty()){
            return false;
        }
        //1.1是否启用
        extracted(addCollegeUserDto.getCollegeGroupId());

        //2.人员信息
        CollegeData data = BeanCopyUtils.copyBean(addCollegeUserDto, CollegeData.class);
        if (this.save(data)){
            //给新增的人员添加,学院角色
            this.addCollegeRole(addCollegeUserDto.getUserId());//存在角色，则不添加,不存在则添加
            return true;
        }
        return false;
    }

    private void extracted(Long collegeGroupId) {
        int count = collegeDataMapper.isCollegeGroupStatus(collegeGroupId);
        if (count == 0){
            throw new ServiceException("学院组未启用");
        }
    }

    @Override
    public void addCollegeRole(Long userId) {
        if (userId == null && userId < 0){
            return ;
        }
        //1.先判断是否存在数据库当中
        List<CollegeData> list = this.lambdaQuery()
                .eq(CollegeData::getUserId, userId)
                .list();
        if (list.isEmpty()){
            return ;
        }
        //2.先判断是否已经存在角色
        int count =roleMapper.isUserHasRole(userId, RoleConstant.COLLEGE);
        if (count > 0){
            return ;
        }
        //3.不存在则添加角色
        List<Long> roleId = roleMapper.getRoleIdByRoleKey(RoleConstant.COLLEGE);
        //4.添加角色
        int insert = userRoleMapper.insert(new UserRole(userId, roleId.get(0)));
        if (insert > 0){
            return ;
        }
        throw new ServiceException("添加失败");
    }

    @Override
    public void deleteColleRole(Long userId) {
        //1.判断是否为空
        if (userId == null && userId < 0){
            return ;
        }
        List<CollegeData> list = this.lambdaQuery()
                .eq(CollegeData::getUserId, userId)
                .list();
        //2.还有存在则不删除
        if (!list.isEmpty()){
            return ;
        }
        //3. 判断是否已经存在角色
        int count =roleMapper.isUserHasRole(userId, RoleConstant.COLLEGE);
        if (count > 0){
            return ;
        }
        //4.再判断有该角色则删除
        List<Long> roleId = roleMapper.getRoleIdByRoleKey(RoleConstant.COLLEGE);
        int i = userRoleMapper.deleteById(new UserRole(userId, roleId.get(0)));
        if (i > 0){
            return ;
        }
        throw new ServiceException("删除失败");
    }

    @Override
    public boolean deleteCollegeUser(Long collegeGroupId, Long userId) {
        //1.判断学院和用户是否存在
        List<CollegeData> list = lambdaQuery().eq(CollegeData::getCollegeGroupId, collegeGroupId)
                .eq(CollegeData::getUserId, userId)
                .list();
        if (list.isEmpty()){
           throw new ServiceException("学院或用户不存在");
        }
        //1.1是否启用 判断是否启用
        extracted(collegeGroupId);
        //2.删除
        if (this.removeById(list.get(0).getCollegeDataId())){
            this.deleteColleRole(userId);//删除角色
            return true;
        }
        return false;
    }


}

