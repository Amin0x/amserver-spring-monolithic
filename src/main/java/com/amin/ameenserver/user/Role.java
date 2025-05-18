package com.amin.ameenserver.user;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "roles")
public class Role implements Serializable {

    public static final String USER_ADMIN = "ADMIN";
    public static final String USER_DRIVER = "DRIVER";
    public static final String USER_RIDER = "RIDER";
    public static final String USER_USER = "USER";
    public static final String USER_OPERATOR = "OPERATOR";
    public static final String USER_TRANS = "TRANSACTION";



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private String id;
    private boolean enabled;

    public Role(String id, boolean enabled) {
        this.id = id;
        this.enabled = enabled;
    }

    public Role() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
