package com.amin.ameenserver.order;

import lombok.Data;

@Data
public class OrderAcceptByOtherEvent {
    private long orderId;
    private long bidId;

    public OrderAcceptByOtherEvent(long orderId, long bidId) {
        this.orderId = orderId;
        this.bidId = bidId;
    }
}
