package ru.sapegin.dto;

public record MetricDTO(String methodSignature, long executionTime, String args) {
}
