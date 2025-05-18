package com.amin.ameenserver.order;

import com.amin.ameenserver.KafkaConfig;
import com.amin.ameenserver.MyWebSocketMessage;
import com.amin.ameenserver.user.User;
import com.amin.ameenserver.user.UserRepository;
import com.amin.ameenserver.util.LocationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderDispatchService {

    private final int round = 3;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public OrderDispatchService() {
    }

    @Async
    public void dispatch(Order order, int distance) {
        //get any driver around me
        /*GeoResults<RedisGeoCommands.GeoLocation<Object>> geoResults = orderService.getNearDrivers(
                order.getSourceLatitude(), order.getSourceLongitude(), 5000);

        if (geoResults != null) {
            List<GeoResult<RedisGeoCommands.GeoLocation<Object>>> content = geoResults.getContent();
            for(GeoResult<RedisGeoCommands.GeoLocation<Object>> result: content){
                Distance distance2 = result.getDistance();
                String name = (String) result.getContent().getName();

                //check driver is in ride or unavailable
                User user = userRepository.findByIdAndStatus(name, 1);

                if (user != null){
                    MyWebSocketMessage message = new MyWebSocketMessage();
                    message.setMessageType("ORDER_CREATE");
                    message.setPayload("hi am new order");
                    message.setFrom("");
                    message.setTo(user.getUsername());
                    sendNewOrderEvent(message);
                    System.out.println("new order sent to kafka order....");
                } else {
                    System.out.println("user is not found....");
                }
            }
        }*/

        List<User> users = userRepository.findByLocationAndDistance(order.getSourceLatitude(), order.getSourceLongitude(), distance);
        if (users.isEmpty()){
            //kafkaTemplate.send();
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

        users.forEach(user -> {
            //kafkaTemplate.send();
        });

    }


    //sent to kafka
    protected void sendNewOrderEvent(MyWebSocketMessage message){
        kafkaTemplate.send(KafkaConfig.TOPIC_ORDER, message);
    }

}
