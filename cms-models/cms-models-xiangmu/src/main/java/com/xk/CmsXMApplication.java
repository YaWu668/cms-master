package com.xk;

import com.cms.common.security.annotation.EnableCustomConfig;
import com.cms.common.security.annotation.EnableRyFeignClients;
import com.cms.common.swagger.annotation.EnableCustomSwagger2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableCustomConfig
@EnableRyFeignClients
@MapperScan("com.xk.mapper")
@SpringBootApplication
public class CmsXMApplication {
    public static void main(String[] args) {
        SpringApplication.run(CmsXMApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  一生一项目   ლ(´ڡ`ლ)ﾞ  \n" +
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
