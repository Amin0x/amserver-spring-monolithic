package com.amin.ameenserver.order;

import java.io.Serializable;


public class BidDto implements Serializable {
    private Long id;
    private int price;
    private String status;
    private Long userId;
    private Long orderId;
    private int rideCount;
    private int rideAcceptedCount;
    private long ridesDone;
    private int likes;
    private int points;
    private boolean ac;
    private boolean wifi;
    private boolean charger;
    private boolean water;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public int getRideCount() {
        return rideCount;
    }

    public void setRideCount(int rideCount) {
        this.rideCount = rideCount;
    }

    public int getRideAcceptedCount() {
        return rideAcceptedCount;
    }

    public void setRideAcceptedCount(int rideAcceptedCount) {
        this.rideAcceptedCount = rideAcceptedCount;
    }

    public long getRidesDone() {
        return ridesDone;
    }

    public void setRidesDone(long ridesDone) {
        this.ridesDone = ridesDone;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isAc() {
        return ac;
    }

    public void setAc(boolean ac) {
        this.ac = ac;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isCharger() {
        return charger;
    }

    public void setCharger(boolean charger) {
        this.charger = charger;
    }

    public boolean isWater() {
        return water;
    }

    public void setWater(boolean water) {
        this.water = water;
    }
}
