package com.amin.ameenserver.order;

import com.amin.ameenserver.core.ResourceNotFoundException;
import com.amin.ameenserver.user.User;
import com.amin.ameenserver.user.UserRepository;
import com.amin.ameenserver.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final KafkaTemplate<String, Object> template;
    private final UserService userService;

    @Autowired
    public OrderController( OrderService orderService, KafkaTemplate<String, Object> template, UserService userService) {
        
        this.orderService = orderService;
        this.template = template;
        this.userService = userService;
    }

    // get order by id
    @GetMapping("/api/v1/orders/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable(value = "orderId") long orderId, Authentication authentication) {

        Order order = orderService.getOrder(orderId);
        if (authentication.isAuthenticated()){
            User user = UserService.getUser(authentication.getName(), authentication.getAuthorities().stream().findFirst().getAuthority());
            if(!Objects.equals(user.getId(), order.getRider().getId())){
                if (order.getDriver() == null || !Objects.equals(user.getId(), order.getDriver().getId())){
                    throw new ResourceNotFoundException("not authorized");
                }
            }
        } else {
            throw new ResourceNotFoundException("");
        }
//        if (!Objects.equals(order.getRider().getUsername(), SecurityContextHolder.getContext().getAuthentication().getName())){
//            throw new ResourceNotFoundException("Order not found with id :" + orderId);
//        }
        return ResponseEntity.ok(order);
    }

    //create order
    @PostMapping("/api/v1/orders")
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderDto order, Authentication authentication) {
        log.info("createOrder with CreateOrderDto=" + order);
        User user = userService.getAuthUser();

        if(user == null){
            log.error("user is null");
            throw new ResourceNotFoundException("not authorized");
        }

        Order order1 = this.orderService.createOrder(order, user);

        return ResponseEntity.ok(order1);
    }

    @PostMapping("/api/v1/orders/{orderId}/cancelOrder")
    public ResponseEntity<Order>  cancelOrder(@PathVariable("orderId") long orderId, Authentication authentication) {
        Order order = orderService.cancelOrder(orderId, 0L, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    @PostMapping("/api/v1/orders/{orderId}/bids/{bidId}/acceptOrder")
    public ResponseEntity<Order> acceptBid(@PathVariable("bidId") long bidId,
                                           @PathVariable("orderId") long orderId,
                                           Authentication authentication) {

        orderService.acceptBid(bidId, orderId, authentication);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/api/v1/orders/{orderId}/bids")
    public ResponseEntity<Bid> createBid(@RequestBody CreateBidDto bid, @PathVariable long orderId) {
        Bid saveBid = orderService.createBid(bid, orderId);
        return ResponseEntity.ok().body(saveBid);
    }

    //driver reject order (only save data to analysis)

    @PostMapping("/api/v1/orders/{orderId}/bids/{bidId}/rejectBid")
    public ResponseEntity<Order> rejectBid(@PathVariable long orderId, @PathVariable long bidId, Authentication authentication) {
        Order order = orderService.getOrder(orderId);
        Bid bid = orderService.getBid(bidId);

        if (!bid.getOrder().getId().equals(bidId)){
            throw new IllegalArgumentException("");
        }

        if (bid.getStatus() == Bid.STATUS_REJECTED){
            throw new IllegalArgumentException("");
        }

        if (!((User)authentication.getPrincipal()).getRider().getId().equals(order.getRider().getId())){
            throw new RuntimeException();
        }

        orderService.rejectBid(bid);

        template.send("TOPIC_BID_REJECT", bid);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/api/v1/orders/{orderId}/arriveOrder")
    public ResponseEntity<Order> driverArrived(@PathVariable(name = "orderId") long orderId) {

        Order order = orderService.driverArrived(orderId);
        return ResponseEntity.ok().body(order);
    }

    @PostMapping("/api/v1/orders/{orderId}/startOrder")
    public ResponseEntity<Order> rideStarted(@PathVariable(name = "orderId") long orderId) {

        Order order = orderService.rideStarted(orderId);
        return ResponseEntity.ok().body(order);
    }

    @PostMapping("/api/v1/orders/{orderId}/finishOrder")
    public ResponseEntity<Order> rideFinished(@PathVariable(name = "orderId") long orderId) {

        Order order = orderService.rideFinished(orderId);
        return ResponseEntity.ok().body(order);
    }

    @PostMapping("/api/v1/orders/{orderId}/rateOrder")
    public ResponseEntity<Order> rateRide(@PathVariable(name = "orderId") long orderId,
                                          @RequestBody OrderRateDto orderRateDto,
                                          Authentication authentication) {

        Order order = orderService.rateOrder(orderId, orderRateDto, authentication);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/api/v1/orders/users/{userId}/getActiveOrder")
    public ResponseEntity<Order> getActiveOrder(@PathVariable("userId") long userId, Authentication authentication){
        Order order = userService.getLastOrder(userId, authentication);
        return ResponseEntity.ok(order);
    }

    // get order by id
    @GetMapping("/admin/orders/{orderId}")
    public ResponseEntity<Order> adminGetOrder(@PathVariable("orderId") long orderId) {

        Order order = orderService.getOrder(orderId);
        return ResponseEntity.ok(order);
    }

    
    //create order
    @PostMapping("/admin/orders")
    public ResponseEntity<Order> adminCreateOrder(@RequestBody CreateOrderDto order) {
        log.info("createOrder with CreateOrderDto=" + order);
        User user = userService.getAuthUser();

        if(user == null){
            log.error("user is null");
            throw new ResourceNotFoundException("not authorized");
        }

        Order order1 = this.orderService.createOrder(order, user);

        return ResponseEntity.ok(order1);
    }
}
