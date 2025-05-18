package com.amin.ameenserver.order;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "order_dispatched")
public class OrderDispatched {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "driver_id", nullable = false)
    private Long driverId;

    @Column(name = "date", nullable = false)
    private Instant date;

    public OrderDispatched() {
    }

    public OrderDispatched(Long orderId, Long driverId, Instant date) {
        this.orderId = orderId;
        this.driverId = driverId;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

}