package com.amin.ameenserver.order;


import com.amin.ameenserver.user.User;
import com.amin.ameenserver.wallet.Transaction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order implements Serializable {

    public static final String ORDER_STATUS_ACTIVE = "ACTIVE";
    public static final String ORDER_STATUS_COMPLETED = "COMPLETED";
    public static final String ORDER_STATUS_CANCELED = "CANCELED";

    public static final int ORDER_TYPE_OPEN = 1;
    public static final int ORDER_TYPE_NORMAL = 2;
    public static final int ORDER_TYPE_FIXED = 3;
    public static final int ORDER_TYPE_SHARED = 4;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_type")
    private Integer orderType;

    @Column(name = "car_class_id")
    private int carClass;

    @Column(name = "source_latitude")
    private double sourceLatitude;

    @Column(name = "source_longitude")
    private double sourceLongitude;

    @Column(name = "destination_latitude")
    private double destinationLatitude;

    @Column(name = "destination_longitude")
    private double destinationLongitude;

    @Column(name = "source_address")
    private String sourceName;

    @Column(name = "destination_address")
    private String destinationName;

    @Column(name = "note")
    private String note;

    @Column(name = "distance")
    private Integer distance;

    @Column(name = "price")
    private Integer price;

    @Column(name = "rider_price")
    private Integer riderPrice;

    @Column(name = "system_price")
    private Integer systemPrice;

    @Column(name = "ac")
    private boolean ac;

    @Column(name = "wifi")
    private boolean wifi;

    @Column(name = "charger")
    private boolean charger;

    @Column(name = "created")
    @Temporal(TemporalType.DATE)
    private Date created;

    @Column(name = "updated")
    @Temporal(TemporalType.DATE)
    private Date updated;

    @Column(name = "smoking", nullable = true)
    private Boolean smoking;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private User driver;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rider_id")
    private User rider;

    @JsonIgnore
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<Transaction> orderTransactions;

    @JsonIgnore
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<Bid> bids;

    @Column(name = "rider_rate")
    private Float riderRate;

    @Column(name = "driver_rate")
    private Float driverRate;

    private String orderStatus;

    private Boolean started;

    private Date startedTime;

    private Boolean arrived;

    private Date arrivedTime;

    private Boolean accepted;

    private Date acceptedTime;

    private Date canceledTime;

    private String cancelReason;

    private Boolean ended;

    private Date endTime;

}
