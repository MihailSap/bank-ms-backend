package ru.sapegin.model;

import jakarta.persistence.*;

@Entity
@Table(name = "token_blacklist")
public class TokenBlacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String body;

    public TokenBlacklist(Long id, String body) {
        this.id = id;
        this.body = body;
    }

    public TokenBlacklist(String body) {
        this.body = body;
    }

    public TokenBlacklist() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String tokenBody) {
        this.body = tokenBody;
    }
}
