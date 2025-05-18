package com.amin.ameenserver.order;

import com.amin.ameenserver.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bids")
public class Bid implements Serializable {

    public static final String STATUS_CREATED = "created";
    public static final String STATUS_REJECTED = "rejected";
    public static final String STATUS_ACCEPTED = "accepted";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "status")
    private String status;

    @Column(name = "distance", columnDefinition = "INT UNSIGNED")
    private Long distance;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated")
    private LocalDateTime updated;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

}