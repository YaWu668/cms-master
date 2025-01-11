package com.xk.config;

import com.github.alenfive.rocketapi.datasource.DataSourceDialect;
import com.github.alenfive.rocketapi.datasource.DataSourceManager;
import com.github.alenfive.rocketapi.datasource.MySQLDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 默认数据源管理器
 */
@Component
@RequiredArgsConstructor
public class DefaultDataSourceManager extends DataSourceManager {

    private final DataSource dataSource;

    @PostConstruct
    public void init() {
        Map<String, DataSourceDialect> dialects = new HashMap<>();
        //不同的数据库使用不同的数据源，这里使用 `MySQLDataSource`
        dialects.put("mysql", new MySQLDataSource(dataSource,true));
        super.setDialectMap(dialects);
    }
}



