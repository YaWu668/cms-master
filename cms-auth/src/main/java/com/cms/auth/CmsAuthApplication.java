package com.cms.auth;

import com.cms.common.security.annotation.EnableRyFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableRyFeignClients
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CmsAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(CmsAuthApplication.class, args);

        System.out.println("(♥◠‿◠)ﾉﾞ  认证模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " ________  ___  ___  _________  ___  ___\n" +
                "|\\   __  \\|\\  \\|\\  \\|\\___   ___\\\\  \\|\\  \\\n" +
                "\\ \\  \\|\\  \\ \\  \\\\\\  \\|___ \\  \\_\\ \\  \\\\  \\\n" +
                " \\ \\   __  \\ \\  \\\\\\  \\   \\ \\  \\ \\ \\   __  \\\n" +
                "  \\ \\  \\ \\  \\ \\ \\\\\\  \\   \\ \\  \\ \\ \\\n" +
                "   \\ \\__\\ \\__\\ \\_______\\   \\ \\__\\ \\ \\__\\ \\__\\\n" +
                "    \\|__|\\|__|\\|_______|    \\|__|  \\|__|\\|__|");
    }
}
