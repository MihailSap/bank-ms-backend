package ru.sapegin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientFastDTO {
    private String firstName;
    private String middleName;
    private String lastName;
    private Long documentId;
}
