package com.krnal.products.scoutinghub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@PropertySource("classpath:config/config.properties")
public class ScoutingHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScoutingHubApplication.class, args);
    }

}
