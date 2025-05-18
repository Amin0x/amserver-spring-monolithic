package com.amin.ameenserver.user;

import com.amin.ameenserver.wallet.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgConstruction
@AllArgConstruction 
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @JsonIgnore
    @Column(name = "remember_token")
    private String rememberToken;

    @Column(name = "verified", nullable = false)
    private boolean verified = false;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "user_mode")
    private Boolean driverMode = false;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @JsonIgnore
    @Column(name = "created")
    private LocalDateTime created;

    @JsonIgnore
    @Column(name = "updated")
    private LocalDateTime updated;

    @JsonIgnore
    @Column(name = "deleted")
    private LocalDateTime deleted;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = Collections.emptySet();

    @JsonIgnore
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Rider rider;

    @JsonIgnore
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Driver driver;

    @JsonIgnore
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private UserDetails userDetails;

    @JsonIgnore
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Account account;

    @Column(name = "name_ar", length = 100)
    private String nameAr;

    @Column(name = "geohash_location", length = 20)
    private String geohashLocation;

}
