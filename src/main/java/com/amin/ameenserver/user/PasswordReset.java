package com.amin.ameenserver.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "password_resets")
public class PasswordReset implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "identity")
    private String identity;

    @Column(name = "token")
    private String token;

    @Column(name = "created_at")
    private Timestamp created_at;


    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
