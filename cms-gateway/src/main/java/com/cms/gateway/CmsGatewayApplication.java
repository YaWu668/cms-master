package com.cms.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CmsGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(CmsGatewayApplication.class, args);

        System.out.println("(♥◠‿◠)ﾉﾞ  网关模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " ________  ________  _________  _______   ___       __   ________      ___    ___\n" +
                " \\   ____\\|\\   __  \\|\\___   ___\\\\  ___ \\ |\\  \\     |\\  \\|\\   __  \\    |\\  \\  /  /|\n" +
                "   \\  \\___|\\ \\  \\|\\  \\|___ \\  \\_\\ \\   __/|\\ \\  \\    \\ \\  \\ \\  \\|\\  \\   \\ \\  \\/  / /\n" +
                "    \\  \\  __\\ \\   __  \\   \\ \\  \\ \\ \\  \\_|/_\\ \\  \\  __\\ \\  \\ \\   __  \\   \\ \\    / / \n" +
                "     \\ \\_______\\ \\__\\ \\__\\   \\ \\__\\ \\ \\_______\\ \\____________\\ \\__\\  \\__/  / /  \n" +
                "      \\|_______|\\|__|\\|__|    \\|__|  \\|_______|\\|____________|\\|__|\\|__|\\___/ / \n" +
                "                                                                      \\|___|/\n");
    }
}
