package com.xk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

    /**
     * 根据字典类型判断字典值是否存在
     * @param dictValue 字典键值
     * @param dictType 字典类型
     * @return true表示存在,false表示不存在
     */
    @Override
    public boolean checkDictData(Long dictValue, String dictType) {
        LambdaQueryWrapper<DictData> queryWrapper= new LambdaQueryWrapper<DictData>()
                .eq(DictData::getDictType,dictType)
                .eq(DictData::getDictValue,dictValue)
                .last("LIMIT 1");
        DictData dictData = this.getOne(queryWrapper);
        if(dictData == null){
            return false;
        }
        return true;
    }
}

