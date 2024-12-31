package com.xk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xk.domain.vo.group.YearDetailedLisVo;
import com.xk.entity.YearGroup;
import org.apache.ibatis.annotations.Param;
import java.util.List;


/**
 * 年度组表(YearGroup)表数据库访问层
 *
 * @author makejava
 * @since 2024-12-05 22:47:31
 */
public interface YearGroupMapper extends BaseMapper<YearGroup> {
    /**
     * 根据动态查询条件查询年度组数据
     *
     * @param yearGroupName 年度组名称
     * @param yearDataName 年度数据名称
     * @return 查询结果
     */
    YearDetailedLisVo selectWithDynamicQuery(@Param("yearGroupName") String yearGroupName, @Param("yearDataName") String yearDataName);

}

