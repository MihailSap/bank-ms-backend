package ru.sapegin.model;

import jakarta.persistence.*;
import ru.sapegin.enums.KeyEnum;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private KeyEnum key;

    private LocalDateTime createDate;

    private String productId;

    public Product(Long id, String name, KeyEnum key, LocalDateTime createDate, String productId) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.createDate = createDate;
        this.productId = productId;
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

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
