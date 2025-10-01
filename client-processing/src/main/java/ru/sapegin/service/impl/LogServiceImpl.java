package ru.sapegin.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sapegin.dto.ErrorLogDTO;
import ru.sapegin.dto.RequestLogDTO;
import ru.sapegin.model.ErrorLog;
import ru.sapegin.repository.ErrorLogRepository;
import ru.sapegin.service.LogServiceI;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogServiceI {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final ErrorLogRepository errorLogRepository;

    private final ObjectMapper objectMapper;

    @Transactional
    @Override
    public void create(ErrorLogDTO errorLogDTO){
        var errorLog = new ErrorLog();
        errorLog.setTimestamp(errorLogDTO.timestamp());
        errorLog.setMethodSignature(errorLogDTO.methodSignature());
        errorLog.setStacktrace(errorLogDTO.stacktrace());
        errorLog.setExceptionMessage(errorLogDTO.exceptionMessage());
        errorLog.setMethodParams(errorLogDTO.methodParams());
        errorLogRepository.save(errorLog);
    }

    @Override
    public String getParsedParams(String params){
        if(params == null){
            return null;
        }
        var a = params.split("&");
        return Arrays.toString(a);
    }

    @Override
    public String getBody(Object result){
        String body;
        try{
            body = objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            body = result.toString();
        }
        return body;
    }

    @Override
    public void sendRequestLog(RequestLogDTO requestLogDTO, String applicationName){
        try{
            var record = new ProducerRecord<String, Object>("service_logs", applicationName, requestLogDTO);
            record.headers().add(new RecordHeader("type", "INFO".getBytes(StandardCharsets.UTF_8)));
            kafkaTemplate.send(record);
            log.info("Сообщение отправлено в топик");
        } catch (Exception ex){
            log.warn("Не удалось отправить сообщение в топик");
        }
    }
}
