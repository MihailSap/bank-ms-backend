package ru.sapegin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientProcessingApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClientProcessingApplication.class, args);
        System.out.println("Hello, Client processing!!!");
    }
}
