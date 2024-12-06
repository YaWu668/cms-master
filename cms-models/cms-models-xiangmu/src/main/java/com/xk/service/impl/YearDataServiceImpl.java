package com.xk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xk.entity.YearData;
import com.xk.mapper.YearDataMapper;
import com.xk.service.YearDataService;
import org.springframework.stereotype.Service;

/**
 * 年度组表(YearData)表服务实现类
 *
 * @author makejava
 * @since 2024-12-05 22:52:50
 */
@Service("yearDataService")
public class YearDataServiceImpl extends ServiceImpl<YearDataMapper, YearData> implements YearDataService {

}

