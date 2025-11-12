package ru.sapegin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class AuthProcessingApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthProcessingApplication.class, args);
    }
}