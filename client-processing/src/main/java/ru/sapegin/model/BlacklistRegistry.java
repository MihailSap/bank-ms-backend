package ru.sapegin.model;

import jakarta.persistence.*;
import ru.sapegin.enums.DocumentTypeEnum;

import java.time.LocalDate;

@Entity
@Table(name = "blacklist_registry")
public class BlacklistRegistry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long documentId;

    @Enumerated(EnumType.STRING)
    private DocumentTypeEnum documentType;

    private LocalDate blacklistedAt;

    private String reason;

    private LocalDate blacklistExpirationDate;

    public BlacklistRegistry(Long documentId, DocumentTypeEnum documentType,
                             LocalDate blacklistedAt, String reason, LocalDate blacklistExpirationDate) {
        this.documentId = documentId;
        this.documentType = documentType;
        this.blacklistedAt = blacklistedAt;
        this.reason = reason;
        this.blacklistExpirationDate = blacklistExpirationDate;
    }
    public BlacklistRegistry() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public DocumentTypeEnum getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentTypeEnum documentType) {
        this.documentType = documentType;
    }

    public LocalDate getBlacklistedAt() {
        return blacklistedAt;
    }

    public void setBlacklistedAt(LocalDate blacklistedAt) {
        this.blacklistedAt = blacklistedAt;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDate getBlacklistExpirationDate() {
        return blacklistExpirationDate;
    }

    public void setBlacklistExpirationDate(LocalDate blackExpirationDate) {
        this.blacklistExpirationDate = blackExpirationDate;
    }
}
