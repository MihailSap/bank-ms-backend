package ru.sapegin.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sapegin.dto.ErrorLogDTO;
import ru.sapegin.model.ErrorLog;
import ru.sapegin.repository.ErrorLogRepository;
import ru.sapegin.service.LogServiceI;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogServiceI {

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

    public String getParsedParams(String params){
        if(params == null){
            return null;
        }
        var a = params.split("&");
        return Arrays.toString(a);
    }

    public String getBody(Object result){
        String body;
        try{
            body = objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            body = result.toString();
        }
        return body;
    }
}
