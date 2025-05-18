package com.amin.ameenserver.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "user_details")
public class UserDetails implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "phone_verified")
    private boolean phoneVerified;

    @Column(name = "datebirth")
    private Date datebirth;

    @Column(name = "address")
    private String address;

    @Column(name = "active_status")
    private int activeStatus;

    @JsonIgnore
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    User user;

    @Column(name = "gender", length = 4)
    private String gender;

    @Column(name = "profession")
    private String profession;

    @Column(name = "nationality")
    private String nationality;

    public UserDetails() {
    }

    public UserDetails(String name, String phone, boolean phoneVerified, Date datebirth, String address, int activeStatus, User user) {

        this.name = name;
        this.phone = phone;
        this.phoneVerified = phoneVerified;
        this.datebirth = datebirth;
        this.address = address;
        this.activeStatus = activeStatus;
        this.user = user;
    }
}
