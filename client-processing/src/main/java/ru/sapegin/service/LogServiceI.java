package ru.sapegin.service;

import ru.sapegin.dto.ErrorLogDTO;
import ru.sapegin.dto.RequestLogDTO;

public interface LogServiceI {

    void create(ErrorLogDTO errorLogDTO);

    String getParsedParams(String params);

    String getBody(Object result);

    void sendRequestLog(RequestLogDTO requestLogDTO, String applicationName);
}
