package com.amin.ameenserver;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.MessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    public static final String TOPIC_ORDERS_DISPATCH_MSG = "topic.ameen.orders.messages";
    public static final String TOPIC_ORDER = "topic.ameen.order";
    public static final String TOPIC_GENERAL = "topic.ameen.general";
    public static final String TOPIC_ORDERS_CREATED = "topic.ameen.orders.created";
    public static final String TOPIC_ORDERS_CANCELED = "topic.ameen.orders.canceled";
    public static final String TOPIC_ORDERS_STATUS = "topic.ameen.orders.status";
    public static final String TOPIC_ORDERS_ACCEPT = "topic.ameen.orders.accept";
    public static final String TOPIC_USERS_LOCATION_CHANGED = "topic.ameen.users.location.changed";
    public static final String TOPIC_USERS_NOTIFICATION = "topic.ameen.users.notification";
    public static final String TOPIC_ORDERS_BIDS_CREATED = "topic.ameen.orders.bids.created";
    public static final String TOPIC_ORDERS_BIDS_DELETED = "topic.ameen.orders.bids.deleted";
    public static final String TOPIC_ORDERS_BIDS_REJECT = "topic.ameen.orders.bids.reject";
    public static final String TOPIC_WEBSOCKET_MSG = "topic.ameen.websocket.messages";
    public static final String TOPIC_LOG = "topic.ameen.log";

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(){
        return new KafkaTemplate<String, Object>(kafkaProducerFactory());
    }

//    @Bean
//    public KafkaTemplate<String, Order> kafkaOrderTemplate(){
//        return new KafkaTemplate<String, Order>(kafkaOrderProducerFactory());
//    }
//
//    @Bean
//    public ProducerFactory<String, Order> kafkaOrderProducerFactory(/*SomeBean someBean*/) {
//        Map<String, Object> producerProperties = new HashMap<>();
//        //producerProperties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, MyProducerInterceptor.class.getName());
//        //producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        //producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        return new DefaultKafkaProducerFactory<>(producerProperties);
//    }
//
//    @Bean
//    public ConsumerFactory<String, Order> kafkaOrderConsumerFactory(/*SomeBean someBean*/) {
//        Map<String, Object> consumerProperties = new HashMap<>();
//        //consumerProperties.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, MyConsumerInterceptor.class.getName());
//        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "taxi_server");
//        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        return new DefaultKafkaConsumerFactory<>(consumerProperties);
//    }

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configs.put(AdminClientConfig.SOCKET_CONNECTION_SETUP_TIMEOUT_MAX_MS_CONFIG, "10000");
        configs.put(AdminClientConfig.SOCKET_CONNECTION_SETUP_TIMEOUT_MS_CONFIG, "5000");
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topicWebsocketMsg() {
        return TopicBuilder
                .name(TOPIC_WEBSOCKET_MSG)
                .partitions(1)
                .replicas(1)
                .config(TopicConfig.RETENTION_MS_CONFIG, "86400000") // 24 hours
                .compact()
                .build();
    }

    @Bean
    public NewTopic topicUsersLocationChanged() {
        return TopicBuilder
                .name(TOPIC_USERS_LOCATION_CHANGED)
                .partitions(1)
                .replicas(1)
                .config(TopicConfig.RETENTION_MS_CONFIG, "86400000") // 24 hours
                .compact()
                .build();
    }

    @Bean
    public NewTopic topicOrdersAccept() {
        return TopicBuilder
                .name(TOPIC_ORDERS_ACCEPT)
                .partitions(1)
                .replicas(1)
                .config(TopicConfig.RETENTION_MS_CONFIG, "86400000") // 24 hours
                .build();
    }

    @Bean
    public NewTopic topicOrderStatus() {
        return TopicBuilder
                .name(TOPIC_ORDERS_STATUS)
                .partitions(1)
                .replicas(1)
                .config(TopicConfig.RETENTION_MS_CONFIG, "86400000") // 24 hours
                .build();
    }

    @Bean
    public NewTopic topicLog() {
        return TopicBuilder
                .name(TOPIC_LOG)
                .partitions(1)
                .replicas(1)
                //.config(TopicConfig.RETENTION_MS_CONFIG, "0") //
                .build();
    }

    @Bean
    public NewTopic topicUsersNotification() {
        return TopicBuilder
                .name(TOPIC_USERS_NOTIFICATION)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topicOrderCreated() {
        return TopicBuilder
                .name(TOPIC_ORDERS_CREATED)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topicOrderCanceled() {
        return TopicBuilder
                .name(TOPIC_ORDERS_CANCELED)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topicOrdersBidsCreated() {
        return TopicBuilder
                .name(TOPIC_ORDERS_BIDS_CREATED)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topicOrdersBidsDeleted() {
        return TopicBuilder
                .name(TOPIC_ORDERS_BIDS_DELETED)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topicOrdersBidsReject() {
        return TopicBuilder
                .name(TOPIC_ORDERS_BIDS_REJECT)
                .partitions(1)
                .replicas(1)
                .build();
    }


    @Bean
    public ConsumerFactory<String, Object> kafkaConsumerFactory(/*SomeBean someBean*/) {
        Map<String, Object> consumerProperties = new HashMap<>();
        //consumerProperties.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, MyConsumerInterceptor.class.getName());
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "taxi_server");
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProperties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return new DefaultKafkaConsumerFactory<>(consumerProperties);
    }

    @Bean
    public ProducerFactory<String, Object> kafkaProducerFactory(/*SomeBean someBean*/) {
        Map<String, Object> producerProperties = new HashMap<>();
        //producerProperties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, MyProducerInterceptor.class.getName());
        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        producerProperties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return new DefaultKafkaProducerFactory<>(producerProperties);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaConsumerFactory());
        factory.setConcurrency(3);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }

    @Bean
    public MessageConverter messageConverter(){
        return new JsonMessageConverter();
    }
}
