package com.amin.ameenserver.config;

import com.amin.ameenserver.location.Location;
import com.amin.ameenserver.order.*;
import com.amin.ameenserver.user.User;
import com.amin.ameenserver.user.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.BoundGeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class KafkaProcess {

    private final WebSocketService webSocketService;
    private final KafkaTemplate<String,Object> kafkaTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaProcess(WebSocketService webSocketService, KafkaTemplate<String, Object> kafkaTemplate, RedisTemplate<String, Object> redisTemplate, OrderRepository orderRepository, UserRepository userRepository, OrderService orderService, ObjectMapper objectMapper) {
        this.webSocketService = webSocketService;
        this.kafkaTemplate = kafkaTemplate;
        this.redisTemplate = redisTemplate;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    //todo no need for this
    @KafkaListener(topics= KafkaConfig.TOPIC_USERS_LOCATION_CHANGED)
    public void userLocationListener(Location location){

        Order order = location.getOrder();

        if (order == null)
            return;

        if (Objects.equals(order.getRider().getId(), location.getUser().getId())) {

            if (Objects.equals(order.getOrderStatus(), Order.ORDER_STATUS_ACTIVE)) {

                try {

                    MyWebSocketMessage myWebSocketMessage = new MyWebSocketMessage();
                    myWebSocketMessage.setMessageId(0);
                    myWebSocketMessage.setMessageType(WebSocketConfig.USER_LOCATION);
                    myWebSocketMessage.setPayload(objectMapper.writeValueAsString(location));
                    myWebSocketMessage.setTo("");//todo: get user
                    myWebSocketMessage.setFrom(""); //todo: get user

                    webSocketService.sendMessage(order.getDriver().getUsername(), objectMapper.writeValueAsString(myWebSocketMessage));

                } catch (IOException e) {
                    log.error("Error Cant Send Websocket Message : ", e);
                }
            }
        }
    }


    @KafkaListener(topics = KafkaConfig.TOPIC_USERS_NOTIFICATION)
    public void messageListener(String data, @Header String to){
        webSocketService.sendMessage(to, data);
    }

    @KafkaListener(topics = KafkaConfig.TOPIC_ORDERS_CREATED, groupId = "new_order_group")
    public void orderCreatedProcess(Order order){
        log.info("kafka consumer new order event:" + order);

        MyWebSocketMessage msg = new MyWebSocketMessage();
        msg.setMessageType(WebSocketConfig.NEW_ORDER);
        msg.setMessageId(System.currentTimeMillis());

        try {
            msg.setPayload(objectMapper.writeValueAsString(order));
        } catch (JsonProcessingException e) {
            log.error("Create order error: ", e);
            return;
        }

        webSocketService.sendBroadcastMessage(msg);
        //List<User> users = userRepository.findByLocationAndDistance(order.getSourceLatitude(), order.getSourceLongitude(), 3000);
        if (order.getRider() == null)
            return;

        String geohashLocation = order.getRider().getGeohashLocation();
        if (geohashLocation.length() == 6){
            //todo:
        }

        geohashLocation = geohashLocation.substring(0, geohashLocation.length()-2);
        List<User> users = userRepository.findByLocationApproximately(geohashLocation);

        if (users.isEmpty()){
            // no driver in this area
            return;
        }

        for (User user : users) {
            log.info("send websocket message to user:" + user + " order: " + order);
            kafkaTemplate.send(KafkaConfig.TOPIC_WEBSOCKET_MSG, user.getUsername(), order);
        }
    }

    @KafkaListener(topics = KafkaConfig.TOPIC_ORDERS_BIDS_CREATED)
    public void bidCreatedListener(OrderBiddingEvent orderBiddingEvent){
        log.info("kafka consumer new order bid :{}", orderBiddingEvent);
        try {
            orderService.getBid(orderBiddingEvent.getBidId());
        } catch (Exception e) {
            log.error("Error Cant Send Websocket Message : ", e);
        }
    }

    @KafkaListener(topics = KafkaConfig.TOPIC_ORDERS_ACCEPT)
    public void orderAccept(Order order){
        log.info("kafka consumer order accept message :{}", order);
        User orderDriver = order.getDriver();

        //send notification to other bidder
        for (Bid bid : order.getBids()) {
            User user = bid.getUser();
            if (orderDriver.getId() != user.getId()){
                try {
                    OrderAcceptByOtherEvent orderAcceptByOtherEvent = new OrderAcceptByOtherEvent(order.getId(), bid.getId());
                    MyWebSocketMessage msg = new MyWebSocketMessage();
                    msg.setPayload(objectMapper.writeValueAsString(msg));
                    msg.setTo(user.getUsername());
                    msg.setFrom("");
                    msg.setMessageType(WebSocketConfig.ORDER_ACCEPT_OTHER);
                    webSocketService.sendMessage(user.getUsername(), objectMapper.writeValueAsString(msg));
                    log.info("send order accept by other notification to {} ", user.getUsername());
                } catch (JsonProcessingException e) {
                    log.error("cant send websocket msg", e);
                }
            }

        }

        //send notification to driver
        try {
            MyWebSocketMessage msgToDriver = new MyWebSocketMessage();
            msgToDriver.setPayload(objectMapper.writeValueAsString(order));
            msgToDriver.setMessageType(WebSocketConfig.ORDER_BID_ACCEPTED);
            msgToDriver.setMessageId(0);
            webSocketService.sendMessage(orderDriver.getUsername(), objectMapper.writeValueAsString(msgToDriver));
        } catch (JsonProcessingException e) {
            log.error("cant send websocket msg to driver", e);
        }

    }

    @KafkaListener(topics = KafkaConfig.TOPIC_ORDERS_STATUS)
    public void orderStatusChanged(Order order){
        try {
            MyWebSocketMessage message = new MyWebSocketMessage();
            message.setPayload(objectMapper.writeValueAsString(order));
            message.setMessageType(WebSocketConfig.ORDER_STATUS_CHANGED);
            message.setMessageId(0);
            webSocketService.sendMessage(order.getRider().getUsername(), objectMapper.writeValueAsString(message));
        } catch (Exception e) {
            log.error("Error Cant Send Websocket Message : ", e);
        }
    }

//    @KafkaListener(topics = KafkaConfig.TOPIC_ORDERS_STATUS )
//    public void createOrderTest(Order order){
//        try {
//            MyWebSocketMessage message = new MyWebSocketMessage();
//            message.setPayload(objectMapper.writeValueAsString(order));
//            message.setMessageType("ORDER_TEST");
//            message.setMessageId(0);
//            webSocketService.sendMessage(order.getDriver().getUsername(), objectMapper.writeValueAsString(message));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//    }

    private void gg(Order order){
        //get any driver around me
        BoundGeoOperations<String, Object> geoOperations = redisTemplate.boundGeoOps(RedisConfig.DRIVER_LOCATION_KEY);
        Point point = new Point(order.getSourceLatitude(), order.getSourceLongitude());
        Circle circle = new Circle(point, new Distance(3, Metrics.KILOMETERS));
        GeoResults<RedisGeoCommands.GeoLocation<Object>> geoResults = geoOperations.radius(circle);

        if (geoResults != null) {
            List<GeoResult<RedisGeoCommands.GeoLocation<Object>>> content = geoResults.getContent();
            for(GeoResult<RedisGeoCommands.GeoLocation<Object>> result: content){
                Distance distance2 = result.getDistance();
                String name = (String) result.getContent().getName();

                //check driver is in ride or unavailable
                User user = userRepository.findByIdAndStatus(name, 1);

                if (user != null){

                    try {
                        webSocketService.sendMessage(name,  objectMapper.writeValueAsString(order));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                } else {
                    log.error("user is not found....");
                    log.error("Error Cant Send Websocket Message : ");
                }
            }
        }
    }

}
