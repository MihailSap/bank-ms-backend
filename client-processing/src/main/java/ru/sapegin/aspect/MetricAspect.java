package ru.sapegin.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.sapegin.dto.MetricDTO;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class MetricAspect {

    @Value("${limit.time.method-execution}")
    private Long methodExecutionTimeLimit;

    @Value("${spring.application.name}")
    private String applicationName;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final ObjectMapper objectMapper;

    @Pointcut("@annotation(ru.sapegin.aspect.annotation.Metric)")
    public void metricMethods(){
    }

    @Around("metricMethods()")
    public Object checkExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        var methodSignature = joinPoint.getSignature().toString();
        var args = objectMapper.writeValueAsString(joinPoint.getArgs());

        var timeBeforeExecution = System.currentTimeMillis();
        var result = joinPoint.proceed();
        var timeAfterExecution = System.currentTimeMillis();
        var executionTime = timeAfterExecution - timeBeforeExecution;

        var metricDTO = new MetricDTO(methodSignature, executionTime, args);
        var metricStringDTO = objectMapper.writeValueAsString(metricDTO);
        if(executionTime > methodExecutionTimeLimit){
            ProducerRecord<String, Object> producerRecord = new ProducerRecord<>("service_logs", applicationName, metricStringDTO);
            producerRecord.headers().add("type", "WARNING".getBytes());
            kafkaTemplate.send(producerRecord);
            log.warn("Время выполнения {}: {} мс, что превышает лимит в {} мс", methodSignature, executionTime, methodExecutionTimeLimit);
        } else {
            log.info("Время выполнения метода: {} мс", executionTime);
        }
        return result;
    }
}
