package ru.sapegin.model;

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
}
