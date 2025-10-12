package ru.sapegin.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;

    private String password;

    private String email;

    private String role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private RefreshToken refreshToken;

    public User(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }
}
