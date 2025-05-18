package com.amin.ameenserver.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RiderAcceptBiddingDto {
    private long bidId;
    private long driverId;
}
