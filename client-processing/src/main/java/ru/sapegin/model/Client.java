package ru.sapegin.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import ru.sapegin.enums.DocumentTypeEnum;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientId;

    private Long userId;

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

    public Client(String clientId, Long userId, String firstName,
                  String middleName, String lastName, LocalDate dateOfBirth,
                  DocumentTypeEnum documentType, Long documentId, String documentPrefix, String documentSuffix) {
        this.clientId = clientId;
        this.userId = userId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.documentType = documentType;
        this.documentId = documentId;
        this.documentPrefix = documentPrefix;
        this.documentSuffix = documentSuffix;
    }
}
