package ru.sapegin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.sapegin.enums.KeyEnum;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private KeyEnum key;
    private LocalDate createDate;
    private String productId;
}
