package com.amin.ameenserver.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverArriveDto implements Serializable {
    private long orderId;
    private OrderDto order;
}
