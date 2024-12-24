package com.xk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.core.web.domain.Response;
import com.xk.entity.SpecialistGroup;
import com.xk.mapper.SpecialistGroupMapper;
import com.xk.service.SpecialistGroupService;
import org.springframework.stereotype.Service;

@Service("SpecialistGroupService")
public class SpecialistGroupServiceImpl extends ServiceImpl<SpecialistGroupMapper, SpecialistGroup> implements SpecialistGroupService {
    @Override
    public Response insertSpecialistGroup(SpecialistGroup specialistGroup) {
        if (isNameNull(specialistGroup.getName())){
            throw new ServiceException("专家组已存在",444);
        }
        save(specialistGroup);
        return Response.success();
    }

    /**
     * 判断专家时候存在是否为空
     * @param name
     * @return
     */
    public Boolean isNameNull(String name){
        return lambdaQuery()
                .eq(SpecialistGroup::getName, name)
                .exists();
    }
}
