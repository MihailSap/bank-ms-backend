package ru.sapegin.model;

import jakarta.persistence.*;
import ru.sapegin.enums.StatusEnum;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "client_product")
public class ClientProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private LocalDateTime openDate;

    private LocalDateTime closeDate;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    public ClientProduct(Long id, Client client, Product product,
                         LocalDateTime openDate, LocalDateTime closeDate, StatusEnum status) {
        this.id = id;
        this.client = client;
        this.product = product;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.status = status;
    }

    public ClientProduct() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public LocalDateTime getOpenDate() {
        return openDate;
    }

    public void setOpenDate(LocalDateTime openDate) {
        this.openDate = openDate;
    }

    public LocalDateTime getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(LocalDateTime closeDate) {
        this.closeDate = closeDate;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }
}
