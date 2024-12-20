package com.xk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xk.entity.CollegeData;
import com.xk.mapper.CollegeDataMapper;
import com.xk.service.CollegeDataService;
import org.springframework.stereotype.Service;

/**
 * 学院组人员表(CollegeData)表服务实现类
 *
 * @author YaWu
 * @since 2024-12-19 00:56:46
 */
@Service("collegeDataService")
public class CollegeDataServiceImpl extends ServiceImpl<CollegeDataMapper, CollegeData> implements CollegeDataService {

}

