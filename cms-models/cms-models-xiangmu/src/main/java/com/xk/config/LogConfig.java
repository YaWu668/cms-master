package com.xk.config;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
public class LogConfig {

    @PostConstruct
    public void configurePoiTlLogging() {
        // 获取 poi-tl 日志记录器
        Logger poiTlLogger = (Logger) LoggerFactory.getLogger("com.deepoove.poi");
        // 关闭日志
        poiTlLogger.setLevel(Level.OFF);
    }
}