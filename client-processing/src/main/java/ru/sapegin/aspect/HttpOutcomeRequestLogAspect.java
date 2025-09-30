package ru.sapegin.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class HttpOutcomeRequestLogAspect {

    @Value("${spring.application.name}")
    private String applicationName;

    private final ObjectMapper objectMapper;

    @Pointcut("@annotation(ru.sapegin.aspect.annotation.HttpOutcomeRequestLog)")
    public void httpOutcomeRequestMethods() {
    }

    @AfterReturning(pointcut = "httpOutcomeRequestMethods()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result){

    }
}