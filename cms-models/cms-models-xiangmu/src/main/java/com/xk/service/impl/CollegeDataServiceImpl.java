package com.xk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.security.utils.SecurityUtils;
import com.xk.entity.CollegeData;
import com.xk.mapper.CollegeDataMapper;
import com.xk.service.CollegeDataService;
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
public class CollegeDataServiceImpl extends ServiceImpl<CollegeDataMapper, CollegeData> implements CollegeDataService {

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
}

