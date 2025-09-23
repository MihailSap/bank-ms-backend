package ru.sapegin.dto;

import ru.sapegin.enums.DocumentTypeEnum;

import java.time.LocalDate;

public record ClientDTO(String firstName, String middleName, String lastName, LocalDate dateOfBirth,
                        DocumentTypeEnum documentType, Long documentId, String documentPrefix,
                        String documentSuffix, int regionNumber, int bankDivisionNumber) {
}
