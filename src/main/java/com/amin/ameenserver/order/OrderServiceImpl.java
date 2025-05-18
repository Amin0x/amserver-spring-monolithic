package com.amin.ameenserver.order;

import com.amin.ameenserver.KafkaConfig;
import com.amin.ameenserver.RedisConfig;
import com.amin.ameenserver.core.ResourceNotFoundException;
import com.amin.ameenserver.data.DriverFinishRideEvent;
import com.amin.ameenserver.data.OrderCreatedEvent;
import com.amin.ameenserver.user.User;
import com.amin.ameenserver.user.UserRepository;
import com.amin.ameenserver.user.UserService;
import com.amin.ameenserver.util.LocationUtil;
import com.amin.ameenserver.wallet.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.BoundGeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String,Object> kafkaTemplate;
    private final BidRepository bidRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final OrderDispatchedRepository orderDispatchedRepository;
    private final CreditAccountRepository creditAccountRepository;
    private final WalletService walletService;
    private final TransactionRepository transactionRepository;

    //@Autowired
    //@Value("app.fee.percent")
    private final Boolean feePercent = false;

    //@Autowired
    //@Value("app.fee.amount")
    private final int feeAmount = 50_000;

    @Autowired
    public OrderServiceImpl(RedisTemplate<String, Object> redisTemplate, OrderRepository orderRepository, KafkaTemplate<String, Object> kafkaTemplate, BidRepository bidRepository, UserRepository userRepository, UserService userService, OrderDispatchedRepository orderDispatchedRepository, CreditAccountRepository creditAccountRepository, WalletService walletService, TransactionRepository transactionRepository) {
        this.redisTemplate = redisTemplate;
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.bidRepository = bidRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.orderDispatchedRepository = orderDispatchedRepository;
        this.creditAccountRepository = creditAccountRepository;
        this.walletService = walletService;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Order createOrder(CreateOrderDto order, User user) {

        Order newOrder = new Order();
        newOrder.setOrderType(order.getOrderClass());
        newOrder.setRider(user);
        newOrder.setAc(order.isAc());
        newOrder.setWifi(order.isWifi());
        newOrder.setCharger(order.isCharger());
        newOrder.setSmoking(false);
        newOrder.setNote(order.getNote());
        newOrder.setSourceLatitude(order.getSourceLatitude());
        newOrder.setSourceLongitude(order.getSourceLongitude());
        newOrder.setDestinationLatitude(order.getDestinationLatitude());
        newOrder.setDestinationLongitude(order.getDestinationLongitude());
        newOrder.setDestinationName(order.getDestinationAddress());
        newOrder.setSourceName(order.getSourceAddress());
        newOrder.setCreated(new Date());
        newOrder.setOrderStatus(Order.ORDER_STATUS_ACTIVE);
        newOrder.setEnded(false);
        newOrder.setArrived(false);
        newOrder.setStarted(false);
        newOrder.setAccepted(false);

        Order order1 = this.orderRepository.save(newOrder);


        kafkaTemplate.send(KafkaConfig.TOPIC_ORDERS_CREATED, order1);

        //dispatch(order1, 3000);
        //orderProcessorService.dispatch(newOrder, 3000);

        return order1;
    }

    private List<User> sortDriver(List<User> users) {
        if (users != null) {
            return users.stream().sorted((user1, user2) -> {
                return (int) (user1.getId() - user2.getId());
            }).toList();
        }
        return null;
    }


    @Override
    public Order createOrUpdateOrder(Order order) {
        if (order.getId() != null){
            Optional<Order> order1 = orderRepository.findById(order.getId());
            if(order1.isPresent()){
                Order newOrder = order1.get();
                newOrder.setRider(order.getRider());
                newOrder.setDriver(order.getDriver());
                newOrder.setOrderType(order.getOrderType());

                newOrder = this.orderRepository.save(newOrder);
                return newOrder;
            }

        } else {
            order = this.orderRepository.save(order);
            return order;
        }
        return null;
    }

    @Override
    public Order getOrder(long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("order not found"));
    }

    @Override
    public List<Order> getAllOrders(int page) {
        return orderRepository.findAll(Pageable.ofSize(50).withPage(page)).toList();
    }

    @Override
    public List<Bid> getBids(PageRequest pageRequest) {
        return bidRepository.findAll(pageRequest).getContent();
    }

    @Override
    public void notifyUserLocationChanged(Order order) {

    }

    @Override
    public List<Order> getOrdersHistory(User user, int page) {
        if (user.getDriverMode()) {
            List<Order> orders = orderRepository.findByDriverId(user.getId(), PageRequest.of(page, 10));

            return orders;

        } else {
            return orderRepository.findByRiderId(user.getId(), PageRequest.of(page, 10));
        }
    }

    @Override
    public long getRevenue(User user, Date start, Date end) {

        //return orderRepository.sumPriceByIdBetween(user.getId(), start, end);
        return 0;
    }

    @Override
    public Order createTestOrder(Order order) {
        Order order1 = orderRepository.save(order);

        if (order1 != null){
            kafkaTemplate.send(KafkaConfig.TOPIC_ORDERS_CREATED, order1);
        }

        return order1;
    }



    @Override
    public Bid getBid(long bidId) {
        return bidRepository.findById(bidId).orElseThrow(() -> new ResourceNotFoundException("bid not found"));
    }

    @Override
    public Bid createBid(CreateBidDto bid, Long orderId) {
        Order order = this.getOrder(orderId);
        User driver = userService.getAuthUser();

        Bid bid1 = new Bid();
        bid1.setUser(driver);
        bid1.setOrder(order);
        bid1.setPrice(bid.getPrice());
        bid1.setCreated(new Date());
        bid1.setStatus(Bid.STATUS_CREATED);
        bid1.setDistance(3000L);

        Bid saveBid = null;
        try {
            saveBid = bidRepository.save(bid1);
        } catch (Exception e) {
            throw new EntityExistsException("you already bid on this trip");
        }

        //OrderBiddingEvent orderBiddingEvent = new OrderBiddingEvent();
        //orderBiddingEvent.setName(order.getDriver().getUsername());
        //orderBiddingEvent.setImg(order.getRider().getUsername());
        //orderBiddingEvent.setBid((long) bid.getPrice());
        //orderBiddingEvent.rate = userService.getUserRate(driver);
        //orderBiddingEvent.ac = driver.getDriver().isAc();

        //template.send(KafkaConfig.TOPIC_ORDERS_BIDS_CREATED, order.getBids());

        return saveBid;
    }

    @Override
    public Order acceptBid(long bidId, long orderId, Authentication authentication){
        Bid bid = this.getBid(bidId);
        Order order = this.getOrder(orderId);
        //User user = userService.getAuthUser();
        User user = (User) authentication.getPrincipal();

        if (!bid.getOrder().getId().equals(orderId)){
            throw new IllegalArgumentException("bad arguments");
        }

        if (!user.getId().equals(bid.getUser().getId())) {
            throw new AccessDeniedException("");
        }

        if (!order.getOrderStatus().equals(Order.ORDER_STATUS_ACTIVE)) {
            throw new IllegalArgumentException("");
        }

        if (order.getAccepted()) {
            throw new IllegalArgumentException("");
        }

        order.setDriver(user);
        order.setAccepted(true);
        order.setAcceptedTime(new Date());

        //notify rider about order change
        kafkaTemplate.send(KafkaConfig.TOPIC_ORDERS_ACCEPT, order);

        return orderRepository.save(order);
    }

    @Override
    public void deleteBid(Bid bid) {
        User authUser = userService.getAuthUser();
        if (!authUser.getId().equals(bid.getUser().getId()) && !userService.isAdmin(authUser))
            throw new AccessDeniedException("");

        bidRepository.delete(bid);
    }

    @Override
    public Bid getOrderBid(Long orderId, Long driverId) {
        return null;
    }

    @Override
    public Order cancelOrder(Long orderId, Long userId, Authentication authentication) {
        if (authentication == null) {
            throw new AccessDeniedException("error: not authenticated user");
        }

        Order order = this.getOrder(orderId);
        User user = (User) authentication.getPrincipal();

        if (!order.getOrderStatus().equals(Order.ORDER_STATUS_ACTIVE)){
            throw new IllegalArgumentException("");
        }


        if (order.getStarted()){

        } else if (order.getArrived()){

        } else if (order.getAccepted()){
            if (Objects.equals(user.getId(), order.getRider().getId())) {
                long t1 = System.currentTimeMillis();
                order.setOrderStatus(Order.ORDER_STATUS_CANCELED);
                order.setCancelReason("");
                order.setCanceledTime(new Date());
                //todo: send event
                return orderRepository.save(order);
            } else if (Objects.equals(user.getId(), order.getDriver().getId())){

            } else {
                throw new AccessDeniedException("");
            }
        }


        return null;
    }

    @Async
    private void dispatch(Order order, int distance) {
        if (order == null) return;
        if (distance <= 0) return;

        List<User> users = getAllNearDriverDb(order.getRider().getGeohashLocation());

        if (users == null || users.isEmpty()){
            kafkaTemplate.send("ORDER_NO_NEAR_DRIVER_FOUND", order );
            return;
        }

        //sort
        users.sort((user1, user2) -> {
            double distance1 = LocationUtil.distance(user1.getLatitude(), user1.getLongitude(),
                    order.getSourceLatitude(), order.getSourceLongitude(),"k");
            double distance2 = LocationUtil.distance(user2.getLatitude(), user2.getLongitude(),
                    order.getSourceLatitude(), order.getSourceLongitude(),"k");

            long rate1 = user1.getDriver().getRidesDone();
            long rate2 = user2.getDriver().getRidesDone();

            int likes1 = user1.getDriver().getLikes();
            int likes2 = user2.getDriver().getLikes();
            int points1 = user1.getDriver().getPoints();
            int points2 = user2.getDriver().getPoints();

            double w1,w2;

            w1 = (distance1 * 0.60) + (rate1 * 0.10) + (likes1 * 0.10) + (points1 * 0.20);
            w2 = (distance2 * 0.60) + (rate2 * 0.10) + (likes2 * 0.10) + (points2 * 0.20);

            if (w1 == w2)
                return 0;

            return w1 > w2? 1:-1;
        });

        for (User user : users) {
            OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(order, user);
            kafkaTemplate.send(KafkaConfig.TOPIC_ORDERS_CREATED, orderCreatedEvent);
            //kafkaTemplate.send(KafkaConfig.ORDER_DRIVER_DISPATCHED, order);//notify rider

            OrderDispatched orderDispatched = new OrderDispatched(order.getId(), user.getId(), Instant.now());
            orderDispatchedRepository.save(orderDispatched);
        }
    }

    private List<User> getAllNearDriverDb(String geoHash) {
        geoHash = geoHash.substring(0, geoHash.length()-2);
        return userRepository.findByLocationApproximately(geoHash);
    }

    private GeoResults<RedisGeoCommands.GeoLocation<Object>> getNearDriversRedis(double latitude, double longitude, double distance) {
        //get any driver around me
        BoundGeoOperations<String, Object> geoOperations = redisTemplate.boundGeoOps(RedisConfig.DRIVER_LOCATION_KEY);
        Point point = new Point(latitude, longitude);
        Circle circle = new Circle(point, new Distance(distance, Metrics.KILOMETERS));
        GeoResults<RedisGeoCommands.GeoLocation<Object>> geoResults;
        return geoOperations.radius(circle);
    }

    @Override
    public void rejectBid(Bid bid) {

    }

    @Override
    public Order rateOrder(Long orderId, OrderRateDto orderRateDto, Authentication authentication) {
        User user = userService.getUserByUsername(authentication.getName());
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException(""));

        if (Objects.equals(order.getRider().getId(), user.getId())) {
            order.setRiderRate(orderRateDto.getRate());
        } else if (Objects.equals(order.getDriver().getId(), user.getId())) {
            order.setDriverRate(orderRateDto.getRate());
        } else {
            throw new AccessDeniedException("");
        }

        return orderRepository.save(order);
    }

    @Override
    public Order rideFinished(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("order not found"));

        order.setOrderStatus(Order.ORDER_STATUS_COMPLETED);
        order.setEnded(true);
        order.setEndTime(new Date());
        Order save = orderRepository.save(order);

        int fee;

        if (feePercent){
            fee = Math.abs(order.getPrice() * (feeAmount / 100));
        } else {
            fee = feeAmount;
        }

        Transaction transaction = new Transaction();
        transaction.setOrder(order);
        transaction.setAmount(fee * (-1));
        transaction.setCreatedAt(new Date());
        transaction.setDescription("");
        transaction.setFromAccount(order.getDriver().getAccount().getId());
        transaction.setToAccount(creditAccountRepository.findFeeAccountId());
        transaction.setType(TransactionType.FEE);
        transactionRepository.save(transaction);

        DriverFinishRideEvent finishRideDto1 = new DriverFinishRideEvent();

        kafkaTemplate.send("rideFinished", finishRideDto1);
        return save;
    }

    @Override
    public Order rideStarted(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("order not found"));

        order.setStarted(true);
        order.setStartedTime(new Date());
        return orderRepository.save(order);
    }

    @Override
    public Order driverArrived(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("order not found"));

        order.setArrived(true);
        order.setArrivedTime(new Date());
        return orderRepository.save(order);
    }
}
