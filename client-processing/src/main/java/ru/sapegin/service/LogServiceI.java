package ru.sapegin.service;

import ru.sapegin.dto.ErrorLogDTO;

public interface LogServiceI {

    void create(ErrorLogDTO errorLogDTO);
}
