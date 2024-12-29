package com.xk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.security.utils.SecurityUtils;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.core.web.domain.Response;
import com.xk.domain.dto.AddSpecialistUserDTO;
import com.xk.entity.SpecialistData;
import com.xk.entity.SpecialistGroup;
import com.xk.entity.User;
import com.xk.mapper.SpecialistDataMapper;
import com.xk.mapper.SpecialistGroupMapper;
import com.xk.mapper.UserMapper;
import com.xk.service.SpecialistDataService;
import com.xk.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 专家人员表(SpecialistData)表服务实现类
 *
 * @author YaWu
 * @since 2024-12-19 01:42:02
 */
@Service("specialistDataService")
public class SpecialistDataServiceImpl extends ServiceImpl<SpecialistDataMapper, SpecialistData> implements SpecialistDataService {

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
    @Resource
    private UserMapper userMapper;

    @Resource
    private SpecialistGroupMapper specialistGroupMapper;

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

