package com.amin.ameenserver.order;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderBiddingEvent implements Serializable {
    public long bidId;
    public long driverId;
    public long orderId;
    public String name;
    public String img;
    public String car;
    public double rate;
    public long points;
    public long likes;
    public boolean ac;
    public boolean wifi;
    public boolean water;
    public boolean charger;
}
