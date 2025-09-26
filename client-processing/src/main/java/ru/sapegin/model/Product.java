package ru.sapegin.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.sapegin.enums.KeyEnum;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private KeyEnum key;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createDate;

    private String productId;

    public Product(String name, KeyEnum key, LocalDate createDate) {
        this.name = name;
        this.key = key;
        this.createDate = createDate;
    }

    @PostPersist
    public void assignProductId() {
        if (this.productId == null) {
            this.productId = String.format("%s%d", this.key, this.id);
        }
    }
}
