package com.amin.ameenserver.user;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "drivers")
@Getter
@Setter
@NoArgsConstructor
public class Driver implements Serializable {

    @Id
    private Long userId;

    @Column(nullable = false)
    private long carId;

    @Column(nullable = false, unique = true)
    private String drivingLicenceNumber;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date drivingLicenceExpireDate;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date drivingLicenceIssueDate;

    @Column(nullable = false)
    private String drivingLicenceImage;

    @Column(unique = true, nullable = false)
    private String governmentIdNumber;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date governmentIdExpireDate;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date governmentIdIssueDate;

    @Column(nullable = false)
    private String governmentIdImage;

    @Column
    private boolean enabledDriver;

    @Column
    private boolean active;

    @Column
    private int rideCount = 0;

    @Column
    private int rideAcceptedCount = 0;

    @Column("rides_done")
    private long ridesDone = 0;

    @Column("likes")
    private int likes = 0;

    @Column("points")
    private int points = 0;

    @Column("ac")
    private boolean ac = false;

    @Column("wifi")
    private boolean wifi = false;

    @Column("charger")
    private boolean charger = false;

    @Column("water")
    private boolean water = false;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime created;

    @MapsId
    @JsonIgnore
    @OneToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "userDriver")
    private List<UserCar> cars;

    public Driver(User userId, String drivingLicenceNumber, Date drivingLicenceExpireDate,
                  Date drivingLicenceIssueDate, String drivingLicenceImage, String governmentIdNumber,
                  Date governmentIdExpireDate, Date governmentIdIssueDate, String governmentIdImage) {

    }

    
}
