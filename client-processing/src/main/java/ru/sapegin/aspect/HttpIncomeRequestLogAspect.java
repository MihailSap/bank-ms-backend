package ru.sapegin.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class HttpIncomeRequestLogAspect {

    @Pointcut("@annotation(ru.sapegin.aspect.annotation.HttpIncomeRequestLog)")
    public void HttpIncomeRequestMethods(){
    }

    @Before("HttpIncomeRequestMethods()")
    public void logBefore(JoinPoint joinPoint){
    }
}
