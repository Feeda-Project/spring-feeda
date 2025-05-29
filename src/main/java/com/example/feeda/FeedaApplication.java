package com.example.feeda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FeedaApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeedaApplication.class, args);
    }

}
