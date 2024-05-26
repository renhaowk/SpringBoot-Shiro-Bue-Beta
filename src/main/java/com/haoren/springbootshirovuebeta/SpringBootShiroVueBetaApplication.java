package com.haoren.springbootshirovuebeta;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.haoren.springbootshirovuebeta.dao")
public class SpringBootShiroVueBetaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootShiroVueBetaApplication.class, args);
    }

}
