package com.amin.ameenserver.data;

import com.amin.ameenserver.order.Order;
import com.amin.ameenserver.user.User;

public class OrderCreatedEvent {
    private Order order;
    private User user; //user driver to send event;

    public OrderCreatedEvent(Order order, User user) {
        this.order = order;
        this.user = user;
    }

    public OrderCreatedEvent() {
    }
}
