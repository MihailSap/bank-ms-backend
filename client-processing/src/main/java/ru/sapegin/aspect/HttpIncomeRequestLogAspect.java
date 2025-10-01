package ru.sapegin.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.sapegin.dto.RequestLogDTO;

import java.time.Instant;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class HttpIncomeRequestLogAspect {

    @Value("${spring.application.name}")
    private String applicationName;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Pointcut("@annotation(ru.sapegin.aspect.annotation.HttpIncomeRequestLog)")
    public void HttpIncomeRequestMethods(){
    }

    @Before("HttpIncomeRequestMethods()")
    public void logBefore(JoinPoint joinPoint)  {
        var request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        var timestamp = Instant.now().toString();
        var methodSignature = joinPoint.getSignature().toString();
        var uri = request.getRequestURI();
        var queryParams = request.getQueryString();
        var parsedParams = getParsedParams(queryParams);
        var body = Arrays.toString(joinPoint.getArgs());
        var requestLogDTO = new RequestLogDTO(timestamp, methodSignature, uri, parsedParams, body);

        try{
            kafkaTemplate.send("service_logs", applicationName, requestLogDTO);
        } catch (Exception ex){
            log.warn("Не удалось отправить сообщение в топик");
        }

        log.info("BEFORE LOG timestamp: {}; method: {}; uri: {}; params: {}; body: {}",
                timestamp, methodSignature, uri, parsedParams, body);
    }

    public String getParsedParams(String params){
        if(params == null){
            return null;
        }
        var a = params.split("&");
        return Arrays.toString(a);
    }
}
