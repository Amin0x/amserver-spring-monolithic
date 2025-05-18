package com.amin.ameenserver.order;

import lombok.Data;

@Data
public class CreateOrderDto {
    private long userId;
    private double sourceLatitude;
    private double sourceLongitude;
    private double destinationLatitude;
    private double destinationLongitude;
    private String sourceAddress;
    private String destinationAddress;
    private int orderClass;
    private int price;
    private String note;
    private boolean ac;
    private boolean charger;
    private boolean wifi;

    public CreateOrderDto() {
    }

    public CreateOrderDto(long userId, double sourceLatitude, double sourceLongitude,
                          double destinationLatitude, double destinationLongitude, String sourceAddress,
                          String destinationAddress, int orderClass, int price, String note,
                          boolean ac, boolean charger, boolean wifi, Float riderRate, Float driverRate) {
        this.userId = userId;
        this.sourceLatitude = sourceLatitude;
        this.sourceLongitude = sourceLongitude;
        this.destinationLatitude = destinationLatitude;
        this.destinationLongitude = destinationLongitude;
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.orderClass = orderClass;
        this.price = price;
        this.note = note;
        this.ac = ac;
        this.charger = charger;
        this.wifi = wifi;
    }

}
