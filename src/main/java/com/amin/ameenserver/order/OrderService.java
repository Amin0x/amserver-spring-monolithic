package com.amin.ameenserver.order;

import com.amin.ameenserver.user.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.List;

public interface OrderService {

    Order createOrder(CreateOrderDto order, User user);

    Order cancelOrder(Long orderId, Long userId, Authentication authentication);

    Order createOrUpdateOrder(Order order);

    Order getOrder(long orderId);

    List<Order> getAllOrders(int page);

    List<Bid> getBids(PageRequest pageRequest);

    void notifyUserLocationChanged(Order order);

    List<Order> getOrdersHistory(User user, int page);

    long getRevenue(User user, Date start, Date end);

    Order createTestOrder(Order order);

    Bid getBid(long bidId);

    Bid createBid(CreateBidDto bid, Long orderId);

    Order acceptBid(long bidId, long orderId, Authentication authentication);

    void deleteBid(Bid bid);

    Bid getOrderBid(Long orderId, Long driverId);    

    void rejectBid(Bid bid);

    Order rateOrder(Long orderId, OrderRateDto orderRateDto, Authentication authentication);

    Order rideFinished(Long orderId);

    Order rideStarted(Long orderId);

    Order driverArrived(Long orderId);
}
