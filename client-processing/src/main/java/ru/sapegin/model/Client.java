package ru.sapegin.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import ru.sapegin.enums.DocumentTypeEnum;

import java.time.LocalDate;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id) && Objects.equals(clientId, client.clientId) && Objects.equals(userId, client.userId) && Objects.equals(firstName, client.firstName) && Objects.equals(middleName, client.middleName) && Objects.equals(lastName, client.lastName) && Objects.equals(dateOfBirth, client.dateOfBirth) && documentType == client.documentType && Objects.equals(documentId, client.documentId) && Objects.equals(documentPrefix, client.documentPrefix) && Objects.equals(documentSuffix, client.documentSuffix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientId, userId, firstName, middleName, lastName, dateOfBirth, documentType, documentId, documentPrefix, documentSuffix);
    }
}
