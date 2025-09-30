package ru.sapegin.dto;

public record ErrorLogDTO(String timestamp,
                          String methodSignature,
                          String stacktrace,
                          String exceptionMessage,
                          String methodParams) {
}
