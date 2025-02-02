package com.xk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.security.utils.SecurityUtils;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.core.web.domain.Response;
import com.xk.constant.ProjectConstant;
import com.xk.constant.RoleConstant;
import com.xk.domain.dto.AddSpecialistDataDto;
import com.xk.domain.dto.AddSpecialistUserDTO;
import com.xk.domain.dto.PageDTO;
import com.xk.domain.dto.QuerySpecialistData;
import com.xk.domain.vo.specialist.SpecialistDataVo;
import com.xk.entity.SpecialistData;
import com.xk.entity.SpecialistGroup;
import com.xk.entity.User;
import com.xk.entity.UserRole;
import com.xk.mapper.*;
import com.xk.service.SpecialistDataService;
import com.xk.service.UserService;
import com.xk.utils.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 专家人员表(SpecialistData)表服务实现类
 *
 * @author YaWu
 * @since 2024-12-19 01:42:02
 */
@Service("specialistDataService")
@RequiredArgsConstructor
public class SpecialistDataServiceImpl extends ServiceImpl<SpecialistDataMapper, SpecialistData> implements SpecialistDataService {
    /**
     * 专家人员的mapp
     */
    private final SpecialistDataMapper specialistDataMapper;
    /**
     * 用户的mapp
     */
    private final UserMapper userMapper;
    /**
     * 专家组的mpp
     */
    private final SpecialistGroupMapper specialistGroupMapper;
    /**
     * 角色的mapp
     */
    private final RoleMapper roleMapper;
    /**
     * 用户关联角色的mapp
     */
    private final UserRoleMapper userRoleMapper;
    /**
     * 用户服务
     */
    private final UserService userService;
    @Override
    public List<Long> getSpecialistIdByUserId() {
        Long userId = SecurityUtils.getUserId();
        return this.lambdaQuery()
                .eq(SpecialistData::getUserId, userId)
                .list()
                .stream()
                .map(specialistData -> specialistData.getSpecialistGroupId())
                .distinct()
                .collect(Collectors.toList());
    }


    @Override
    public Response addSpecialistUser(AddSpecialistUserDTO addSpecialistUserDTO) {

        //1 校验是否存在这个专家组
        if (isNoSpecialistGroup(addSpecialistUserDTO.getSpecialistGroupId()) == null) {
            throw new ServiceException("专家组不存在", 444);
        }
        //2 校验是否存在用户
        //  校验专家组是否已经存在该专家
        addSpecialistUserDTO.getUserIdList().stream().forEach(user -> {
            if (isNoUser(user.getUserId()) == null) {
                throw new ServiceException("用户不存在" + user.getUserId(), 444);
            }
            if (isNoSpecialist(addSpecialistUserDTO.getSpecialistGroupId(), user.getUserId())) {
                throw new ServiceException("专家组已经存在该专家" + user.getUserId(), 444);
            }
        });
        //3 专家组添加人员，添加信息为专家组id，用户id，用户账号，手机号
        List<Long> collect = addSpecialistUserDTO.getUserIdList().stream().map(User::getUserId).collect(Collectors.toList());
        List<User> userSpecialistList = userMapper.selectBatchIds(collect);
        //3.2 添加到专家数据表
        userSpecialistList.stream().forEach(user -> {
            SpecialistData specialistData = BeanCopyUtils.copyBean(user, SpecialistData.class);
            specialistData.setSpecialistGroupId(addSpecialistUserDTO.getSpecialistGroupId());
            save(specialistData);
        });
        return Response.success();
    }

    @Override
    public PageDTO<SpecialistDataVo> getSpecialistDate(QuerySpecialistData querySpecialistData) {
        //1.分页查询
        Page<SpecialistData> page = querySpecialistData.toMpPageDefaultSortByCreateTimeDesc();
        LambdaQueryWrapper<SpecialistData> wrapper = new LambdaQueryWrapper<SpecialistData>()
                .eq(SpecialistData::getSpecialistGroupId, querySpecialistData.getSpecialistGroupId());
        this.page(page, wrapper);
        PageDTO<SpecialistDataVo> pageDTO = PageDTO.of(page, SpecialistDataVo.class);
        //2.查询用户信息封装返回
        List<Long> userIds = pageDTO.getList().stream()
                .map(vo -> vo.getUserId())
                .collect(Collectors.toList());
        //3.根据用户ID集合
        List<User> users = userService.selectByUserIds(userIds);
        pageDTO.getList().stream()
                .forEach(vo -> {
                    //根据用户id进行匹配,只需要一个即可
                    users.stream()
                            .filter(user -> user.getUserId().equals(vo.getUserId()))
                            .limit(1)
                            .forEach(user -> {
                                vo.setNickName(user.getNickName());
                            });
                });
        return pageDTO;
    }

    @Override
    @Transactional
    public boolean deleteSpecialistData(Long id) {
        //1.判断人员是否存在
        SpecialistData byId = this.getById(id);
        if (byId == null){
            throw new ServiceException("人员不存在");
        }
        //2.获取专家组
        SpecialistGroup specialistGroup = specialistGroupMapper.selectById(byId.getSpecialistGroupId());
        //3.判断是否启用
        if (specialistGroup.getStatus().intValue() != ProjectConstant.COLLEGE_GROUP_STATUS_NORMAL){
            throw new ServiceException("当前专家组未启用,请启用再进行删除");
        }
        //4.删除
        if (this.removeById(id)){
            //5.删除用户角色
//            userMapper.deleteUserRole(byId.getUserId());
            this.deleteSpecialistRole(byId.getUserId());//删除当前用户的角色
            return true;
        }
        return false;
    }

    @Override
    public void deleteSpecialistRole(Long userId) {
        //1.判断是否为空
        if (userId == null && userId < 0){
            return ;
        }
        //2.查询判断当前用户,是否还存在其他专家组
        List<SpecialistData> list = this.lambdaQuery()
                .eq(SpecialistData::getUserId, userId)
                .list();
        //2.2还存在专家组当,不删除角色
        if (!list.isEmpty()){
            return;
        }
        //3. 判断是否已经存在角色
        int count =roleMapper.isUserHasRole(userId, RoleConstant.EXPERT);
        if (count < 0){ //如何存在角色,删除角色
            return ;
        }
        //4.再判断有该角色则删除
        List<Long> roleId = roleMapper.getRoleIdByRoleKey(RoleConstant.EXPERT);
//        int i = userRoleMapper.deleteById(new UserRole(userId, roleId.get(0)));
        int i =userRoleMapper.deleteUserRoleById(userId, roleId.get(0));
        if (i > 0){
            return ;
        }
        throw new ServiceException("出现异常,当前用户存在专家组内,又没有专家组的角色,当前异常账号的id:"+userId);
    }

    @Override
    public boolean addSpecialistData(AddSpecialistDataDto addSpecialistDataDto) {
        //1.判断人员是否重新添加
        List<SpecialistData> list = this.lambdaQuery()
                .eq(SpecialistData::getSpecialistGroupId, addSpecialistDataDto.getSpecialistGroupId())
                .eq(SpecialistData::getUserId, addSpecialistDataDto.getUserId())
                .list();
        if(!list.isEmpty()){
            throw new ServiceException("当前用户已经存在当前专家组当中,请不要重复添加");
        }
        //1.1 判断是否启用
        extracted(addSpecialistDataDto.getSpecialistGroupId());
        //2.人员信息
        SpecialistData data = BeanCopyUtils.copyBean(addSpecialistDataDto, SpecialistData.class);
        //3.检查人员是否存在
        User user = userService.getById(addSpecialistDataDto.getUserId());
        if (user == null){
            throw new ServiceException("用户不存在");
        }
        //4.添加人员并且添加角色
        data.setUserName(user.getUserName());//补充数据
        if(this.save(data)){
            this.addSpecialistRole(addSpecialistDataDto.getUserId());
            return true;
        }
        return false;
    }

    public void addSpecialistRole( Long userId) {
        //1.判断是否为空
        if(userId == null ||  userId < 0){
            throw new ServiceException("用户id为空或者不合法");
        }
        //2判断当中用户是否存在专家组当中
        List<SpecialistData> list = this.lambdaQuery()
                .eq(SpecialistData::getUserId, userId)
                .list();
        if (list.isEmpty()){
            throw new ServiceException("当前用户不存在专家组当中,请先添加到专家组");
        }
        //3.判断当前用户是否有该角色
        int count =roleMapper.isUserHasRole(userId, RoleConstant.EXPERT);
        if (count > 0){
            return ;
        }
        //4.添加角色
        List<Long> roleId = roleMapper.getRoleIdByRoleKey(RoleConstant.EXPERT);
        int insert = userRoleMapper.insert(new UserRole(userId, roleId.get(0)));
        if (insert > 0){
            return ;
        }
        throw new ServiceException("专家角色失败添加失败");

    }

    /**
     * 判断专家组是否启用
     * @param specialistGroupId
     */
    private void extracted(Long specialistGroupId) {
        Long count = specialistGroupMapper.selectCount(
                new LambdaQueryWrapper<SpecialistGroup>()
                        .eq(SpecialistGroup::getSpecialistGroupId, specialistGroupId)
                        .eq(SpecialistGroup::getStatus, ProjectConstant.COLLEGE_GROUP_STATUS_NORMAL)

        );
        if (count <= 0){
            throw new ServiceException("当前专家组未启用,请启用再进行添加");
        }
    }

    // 校验是否存在用户信息
    public User isNoUser(Long userId) {
        return userMapper.selectById(userId);
    }

    // 校验专家组是否已经存在该专家
    public boolean isNoSpecialist(Long specialistGroupId, Long userId) {
        return lambdaQuery()
                .eq(SpecialistData::getSpecialistGroupId, specialistGroupId)
                .eq(SpecialistData::getUserId, userId)
                .exists();
    }
    //获取专家组信息
    public SpecialistGroup isNoSpecialistGroup(Long specialistGroupId){
        return specialistGroupMapper.selectByIdForSpecialistGroupId(specialistGroupId);
    }
}

