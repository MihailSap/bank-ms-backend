package ru.sapegin.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.sapegin.dto.RequestLogDTO;
import ru.sapegin.service.impl.LogServiceImpl;

import java.time.Instant;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class HttpOutcomeRequestLogAspect {

    @Value("${spring.application.name}")
    private String applicationName;

    private final LogServiceImpl logService;

    @Pointcut("@annotation(ru.sapegin.aspect.annotation.HttpOutcomeRequestLog)")
    public void httpOutcomeRequestMethods() {
    }

    @AfterReturning(pointcut = "httpOutcomeRequestMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        var request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        var timestamp = Instant.now().toString();
        var methodSignature = joinPoint.getSignature().toString();
        var uri = request.getRequestURI();
        var queryString = request.getQueryString();
        var parsedParams = logService.getParsedParams(queryString);
        var body = logService.getBody(result);
        var requestLogDTO = new RequestLogDTO(timestamp, methodSignature, uri, parsedParams, body);
        logService.sendRequestLog(requestLogDTO, applicationName);
        log.info("AFTER RETURNING LOG timestamp: {}; method: {}; uri: {}; params: {}; body: {}",
                timestamp, methodSignature, uri, parsedParams, body);
    }
}