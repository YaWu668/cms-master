package com.xk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xk.entity.DictData;
import com.xk.mapper.DictDataMapper;
import com.xk.service.DictDataService;
import org.springframework.stereotype.Service;

/**
 * 字典数据表(DictData)表服务实现类
 *
 * @author makejava
 * @since 2024-12-06 00:03:02
 */
@Service("dictDataService")
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData> implements DictDataService {

}

