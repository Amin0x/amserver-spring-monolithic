package com.amin.ameenserver.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "riders")
public class Rider implements Serializable {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "name_ar", nullable = false, length = 100)
    private String nameAr;

    @Column(name = "rides_count", columnDefinition = "INT UNSIGNED not null")
    private Long ridesCount;

    @Column(name = "rides_done", columnDefinition = "INT UNSIGNED not null")
    private Long ridesDone;

    @Column(name = "likes")
    private long likes;

    @Column(name = "points")
    private long points;

    @MapsId
    @OneToOne()
    @JoinColumn(name = "user_id")
    @JsonIgnore
    User user;


    public Rider() {
    }

    public Rider(User user, String name, String nameAr, long ridesCount, long ridesDone, long likes, long points) {
        this.name = name;
        this.nameAr = nameAr;
        this.ridesCount = ridesCount;
        this.ridesDone = ridesDone;
        this.likes = likes;
        this.points = points;
        this.user = user;
    }
}
