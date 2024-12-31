package com.xk.service;

import com.xk.mapper.YearDataMapper;
import com.xk.mapper.YearGroupMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 年度公共依赖的服务
 */
@Service
@RequiredArgsConstructor
@Data
public class YearHelperService {
    private final @Lazy YearDataService yearDataService;
    private final @Lazy YearGroupService yearGroupService;
    private final YearGroupMapper yearGroupMapper;
    private final YearDataMapper yearDataMapper;

    // 提供公用方法，避免 YearGroupService 和 YearDataService 的循环依赖
}
