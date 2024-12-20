package com.xk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xk.entity.SpecialistData;
import com.xk.mapper.SpecialistDataMapper;
import com.xk.service.SpecialistDataService;
import org.springframework.stereotype.Service;

/**
 * 专家人员表(SpecialistData)表服务实现类
 *
 * @author YaWu
 * @since 2024-12-19 01:42:02
 */
@Service("specialistDataService")
public class SpecialistDataServiceImpl extends ServiceImpl<SpecialistDataMapper, SpecialistData> implements SpecialistDataService {

}

