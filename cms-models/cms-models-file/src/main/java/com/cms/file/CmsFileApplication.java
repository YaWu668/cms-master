package com.cms.file;

import com.cms.common.security.annotation.EnableCustomConfig;
import com.cms.common.security.annotation.EnableRyFeignClients;
import com.cms.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCustomSwagger2
@EnableRyFeignClients
@EnableCustomConfig
@EnableScheduling
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CmsFileApplication {
    public static void main(String[] args) {
        SpringApplication.run(CmsFileApplication.class, args);
    }
}
