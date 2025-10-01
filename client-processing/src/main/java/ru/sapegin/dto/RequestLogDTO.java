package ru.sapegin.dto;

public record RequestLogDTO(String timestamp, String methodSignature, String uri, String params, String body) {
}
