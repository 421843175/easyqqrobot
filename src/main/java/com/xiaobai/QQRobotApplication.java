package com.xiaobai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.URISyntaxException;

//import static java.lang.StringTemplate.STR;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy
public class QQRobotApplication {

    public static void main(String[] args) throws URISyntaxException {
        SpringApplication.run(QQRobotApplication.class, args);
    }

}
