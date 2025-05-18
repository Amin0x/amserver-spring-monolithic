package com.amin.ameenserver.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBidDto implements Serializable {
    private long orderId;
    private int price;
    private long driverId;
}
