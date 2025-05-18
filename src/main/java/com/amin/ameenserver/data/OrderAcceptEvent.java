package com.amin.ameenserver.data;

import com.amin.ameenserver.order.Order;

public class OrderAcceptEvent {
    private Order order;

    public OrderAcceptEvent(Order order) {
        this.order = order;
    }
}
