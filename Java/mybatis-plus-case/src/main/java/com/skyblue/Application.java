package com.skyblue;

import com.fengwenyi.apistarter.EnableApiStarter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableApiStarter // 开启注解式全局异常处理
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
