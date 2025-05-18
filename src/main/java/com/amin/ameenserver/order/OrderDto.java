package com.amin.ameenserver.order;

import com.amin.ameenserver.user.UserDto;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class OrderDto implements Serializable {
    private Long id;
    private String orderType;
    private int carClass;
    private double sourceLatitude;
    private double sourceLongitude;
    private double destinationLatitude;
    private double destinationLongitude;
    private String sourceName;
    private String destinationName;
    private String note;
    private int distance;
    private int price;
    private String status;
    private boolean wifi;
    private boolean charger;
    private int driverRate;
    private int riderRate;
    private Date created;
    private Date updated;
    private Boolean smoking;
    private UserDto driver;
    private UserDto rider;
    private BidDto bid;
}
