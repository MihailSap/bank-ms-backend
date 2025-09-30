package ru.sapegin.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sapegin.dto.ErrorLogDTO;
import ru.sapegin.model.ErrorLog;
import ru.sapegin.repository.ErrorLogRepository;
import ru.sapegin.service.ErrorLogServiceI;

@Service
@RequiredArgsConstructor
public class ErrorLogService implements ErrorLogServiceI {

    private final ErrorLogRepository errorLogRepository;

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
}
