package com.xk.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xk.mapper.ConfigMapper;
import com.xk.service.ConfigService;
import lombok.RequiredArgsConstructor;
import com.xk.entity.Config;
import org.springframework.stereotype.Service;

/**
 * 参数配置表(Config)表服务实现类
 *
 * @author yawu
 * @since 2025-02-25 17:16:18
 */
@RequiredArgsConstructor
@Service("configService")
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {

}

