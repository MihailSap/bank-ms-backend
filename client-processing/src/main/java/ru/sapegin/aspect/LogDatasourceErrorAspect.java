package ru.sapegin.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.sapegin.dto.ErrorLogDTO;
import ru.sapegin.service.impl.ErrorLogService;

import java.time.Instant;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogDatasourceErrorAspect {

    @Value("${spring.application.name}")
    private String applicationName;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final ErrorLogService errorLogService;

    @Pointcut("@annotation(ru.sapegin.aspect.annotation.LogDatasourceError)")
    public void errorMethods(){
    }

    @AfterThrowing(pointcut = "errorMethods()", throwing = "exception")
    public void handleException(JoinPoint joinPoint, Throwable exception){
        var timestamp = Instant.now().toString();
        var methodSignature = joinPoint.getSignature().toString();
        var stacktrace = ExceptionUtils.getStackTrace(exception);
        var exceptionMessage = exception.getMessage();
        var methodParams = Arrays.toString(joinPoint.getArgs());
        var errorLogDTO = new ErrorLogDTO(
                timestamp,
                methodSignature,
                stacktrace,
                exceptionMessage,
                methodParams
        );

        try{
            kafkaTemplate.send("service_logs", applicationName, errorLogDTO);
            log.warn("Исключение отправлено в топик service_logs");
        } catch (Exception e){
            errorLogService.create(errorLogDTO);
            log.warn("Не удалось отправить исключение в топик. Оно было сохранено в БД");
        }

        log.error("Исключение перехвачено: {}", errorLogDTO, exception);
    }
}
