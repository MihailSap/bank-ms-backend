package ru.sapegin.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import ru.sapegin.enums.KeyEnum;

import java.time.LocalDate;

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

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public KeyEnum getKey() {
        return key;
    }

    public void setKey(KeyEnum key) {
        this.key = key;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @PostPersist
    public void assignProductId() {
        if (this.productId == null) {
            this.productId = String.format("%s%d", this.key, this.id);
        }
    }
}
