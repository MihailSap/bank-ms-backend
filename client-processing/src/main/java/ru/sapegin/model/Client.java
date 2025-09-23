package ru.sapegin.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import ru.sapegin.enums.DocumentTypeEnum;

import java.time.LocalDate;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String firstName;

    private String middleName;

    private String lastName;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private DocumentTypeEnum documentType;

    private Long documentId;

    private String documentPrefix;

    private String documentSuffix;

    public Client(String clientId, User user, String firstName,
                  String middleName, String lastName, LocalDate dateOfBirth,
                  DocumentTypeEnum documentType, Long documentId, String documentPrefix, String documentSuffix) {
        this.clientId = clientId;
        this.user = user;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.documentType = documentType;
        this.documentId = documentId;
        this.documentPrefix = documentPrefix;
        this.documentSuffix = documentSuffix;
    }

    public Client() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public DocumentTypeEnum getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentTypeEnum documentType) {
        this.documentType = documentType;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getDocumentPrefix() {
        return documentPrefix;
    }

    public void setDocumentPrefix(String documentPrefix) {
        this.documentPrefix = documentPrefix;
    }

    public String getDocumentSuffix() {
        return documentSuffix;
    }

    public void setDocumentSuffix(String documentSuffix) {
        this.documentSuffix = documentSuffix;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
