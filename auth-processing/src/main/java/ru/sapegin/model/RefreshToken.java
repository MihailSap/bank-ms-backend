package ru.sapegin.model;

import jakarta.persistence.*;

@Entity
@Table(name = "token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String body;

    @OneToOne
    @JoinColumn(name = "users_id", nullable = false, unique = true)
    private User user;

    public RefreshToken(String body, User user) {
        this.body = body;
        this.user = user;
    }

    public RefreshToken() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
