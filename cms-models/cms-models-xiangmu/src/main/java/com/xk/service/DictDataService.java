package com.xk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xk.entity.DictData;


/**
 * 字典数据表(DictData)表服务接口
 *
 * @author makejava
 * @since 2024-12-06 00:03:02
 */
public interface DictDataService extends IService<DictData> {
    /**
     * 根据字典类型判断字典值是否存在
     * @param dictValue 字典键值
     * @param dictType 字典类型
     * @return true表示存在,false表示不存在
     */
    boolean checkDictData(Long dictValue, String dictType);
}

