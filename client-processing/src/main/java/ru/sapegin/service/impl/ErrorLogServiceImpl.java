package ru.sapegin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sapegin.dto.ErrorLogDTO;
import ru.sapegin.model.ErrorLog;
import ru.sapegin.repository.ErrorLogRepository;
import ru.sapegin.util.ErrorLogStorage;

@Slf4j
@Service
@RequiredArgsConstructor
public class ErrorLogServiceImpl implements ErrorLogStorage {

    private final ErrorLogRepository errorLogRepository;

    @Override
    public void save(ErrorLogDTO errorLogDTO) {
        var errorLog = new ErrorLog();
        errorLog.setTimestamp(errorLogDTO.timestamp());
        errorLog.setMethodSignature(errorLogDTO.methodSignature());
        errorLog.setStacktrace(errorLogDTO.stacktrace());
        errorLog.setExceptionMessage(errorLogDTO.exceptionMessage());
        errorLog.setMethodParams(errorLogDTO.methodParams());
        errorLogRepository.save(errorLog);
    }
}
