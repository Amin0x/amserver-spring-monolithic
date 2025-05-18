package com.amin.ameenserver.data;

import com.amin.ameenserver.order.OrderDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverFinishRideEvent implements Serializable {
    private long orderId;
    private OrderDto orderDto;
}
