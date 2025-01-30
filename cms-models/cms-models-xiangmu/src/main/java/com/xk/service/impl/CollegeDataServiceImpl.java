package com.xk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.security.utils.SecurityUtils;
import com.xk.constant.ProjectConstant;
import com.xk.constant.RoleConstant;
import com.xk.domain.dto.AddCollegeUserDto;
import com.xk.entity.CollegeData;
import com.xk.entity.CollegeGroup;
import com.xk.entity.User;
import com.xk.entity.UserRole;
import com.xk.mapper.CollegeDataMapper;
import com.xk.mapper.RoleMapper;
import com.xk.mapper.UserRoleMapper;
import com.xk.service.CollegeDataService;
import com.xk.service.UserService;
import com.xk.utils.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final UserService userService;
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
    @Transactional
    public boolean addCollegeUser(AddCollegeUserDto addCollegeUserDto) {
        //1.判断是否重复添加
        List<CollegeData> list = this.lambdaQuery()
                .eq(CollegeData::getCollegeGroupId, addCollegeUserDto.getCollegeGroupId())
                .eq(CollegeData::getUserId, addCollegeUserDto.getUserId())
                .list();
        if (!list.isEmpty()){
            throw new ServiceException("不要重复添加当前用户");
        }
        //1.1是否启用
        extracted(addCollegeUserDto.getCollegeGroupId());

        //2.人员信息
        CollegeData data = BeanCopyUtils.copyBean(addCollegeUserDto, CollegeData.class);
        //3.检查人员是否存在
        User user = userService.getById(addCollegeUserDto.getUserId());
        if(user == null){
            throw new ServiceException("用户不存在");
        }
        //4.保存添加角色
        data.setUserName(user.getUserName());//补充数据
        if (this.save(data)){
            //5.给新增的人员添加,学院角色
            this.addCollegeRole(addCollegeUserDto.getUserId());//存在角色，则不添加,不存在则添加
            return true;
        }
        return false;
    }

    /**
     * 判断学院组是否启用
     * @param collegeGroupId 学院组id
     */
    private void extracted(Long collegeGroupId) {
        int count = collegeDataMapper.isCollegeGroupStatus(collegeGroupId, ProjectConstant.COLLEGE_GROUP_STATUS_NORMAL);
        if (count <= 0){
            throw new ServiceException("学院组未启用");
        }
    }

    @Override
    public void addCollegeRole(Long userId) {
        if (userId == null || userId < 0){
            throw new ServiceException("用户id为空或者不合法");
        }
        //1.先判断是否存在数据库当中
        List<CollegeData> list = this.lambdaQuery()
                .eq(CollegeData::getUserId, userId)
                .list();
        if (list.isEmpty()){
            throw new ServiceException("当前用户不存在学院组,请先添加到学院组当中");
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
        throw new ServiceException("学院审核员角色添加失败");
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
        //2.判断当前用户是否还存在,学院组当中,存在就不删除角色
        if (!list.isEmpty()){
            return ;
        }
        //3. 判断是否已经存在角色
        int count =roleMapper.isUserHasRole(userId, RoleConstant.COLLEGE);
        if (count < 0){ //如何存在角色,删除角色
            return ;
        }
        //4.再判断有该角色则删除
        List<Long> roleId = roleMapper.getRoleIdByRoleKey(RoleConstant.COLLEGE);
//        int i = userRoleMapper.deleteById(new UserRole(userId, roleId.get(0)));
        int i =userRoleMapper.deleteUserRoleById(userId, roleId.get(0));
        if (i > 0){
            return ;
        }
       throw new ServiceException("请联系管理员:出现异常,当前用户存在学院组内,又没有学院组的角色,当前异常账号的id:"+userId);
    }

    @Override
    @Transactional
    public boolean deleteCollegeUser(Long id) {
        //1.1判断人员是否存在
        CollegeData collegeData = this.getById(id);
        if (collegeData == null){
            throw new ServiceException("人员不存在");
        }
        //1.2获取学院组
        CollegeGroup group = collegeDataMapper.getCollegeGroupById(collegeData.getCollegeGroupId());
        //1.3是否启用 判断是否启用
//        extracted(group.getCollegeGroupId()); 优化减少查询
        if(group.getStatus().intValue() != ProjectConstant.COLLEGE_GROUP_STATUS_NORMAL){
            throw new ServiceException("当前学院组未启用,请启用再进行删除");
        }
        //2.删除
        if (this.removeById(id)){
            this.deleteColleRole(collegeData.getUserId());//删除当前用户的角色
            return true;
        }
        return false;
    }


}

