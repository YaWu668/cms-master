package com.cms.system;

import com.cms.common.security.annotation.EnableCustomConfig;
import com.cms.common.security.annotation.EnableRyFeignClients;
import com.cms.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringBootApplication
public class CmsSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(CmsSystemApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  系统模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " ________       ___    ___ ________  _________  _______   _____ ______    \n" +
                "|\\   ____\\\\     |\\  \\  /  /|\\   ____\\|\\___   ___\\\\  ___ \\ |\\   _ \\  _   \\   \n" +
                "\\ \\  \\___|_    \\ \\  \\/  / | \\  \\___\\|\\|___ \\  \\_\\ \\   __/|\\ \\  \\\\\\__\\ \\  \\  \n" +
                " \\ \\_____  \\    \\ \\    / / \\ \\_____  \\   \\ \\  \\ \\ \\  \\_|/_\\ \\  \\\\|__| \\  \\ \n" +
                "  \\|____|\\  \\    \\/  /  /   \\|____|\\  \\   \\ \\  \\ \\ \\  \\_|\\ \\ \\  \\    \\ \\  \\ \n" +
                "    ____\\_\\  \\ __/  / /       ____\\_\\  \\   \\ \\__\\ \\ \\_______\\ \\__\\    \\ \\__\\\n" +
                "   |\\_________\\\\___/ /       |\\_________\\   \\|__|  \\|_______|\\|__|     \\|__|\n" +
                "   \\|_________\\|___|/        \\|_________|                                  ");
    }
}
