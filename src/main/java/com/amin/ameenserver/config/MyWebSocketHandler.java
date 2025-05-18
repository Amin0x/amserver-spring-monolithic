package com.amin.ameenserver;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.security.Principal;

@Component
@Slf4j
@Getter
public class MyWebSocketHandler extends TextWebSocketHandler {

    public static final String MESSAGE_SEND = "SEND_MESSAGE";
    public static final String MESSAGE_LOCATION_UPDATE = "LOCATION_UPDATE";
    public static final String MESSAGE_NEW_ORDER = "NEW_ORDER";
    public static final String MESSAGE_BID = "BID_MESSAGE";
    public static final String MESSAGE_ORDER_DISP = "ORDER_DISP";

    @Autowired
    private WebSocketService webSocketService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //super.afterConnectionEstablished(session);
        Principal principal = session.getPrincipal();

        if (principal != null){
            webSocketService.addWebSocketSession(session);
            return;
        }

        log.debug("principal is null");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        //super.afterConnectionClosed(session, status);
        //session.close();
        Principal principal = session.getPrincipal();

        if (principal != null){
            webSocketService.removeWebSocketSession(session);
        }
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("handleTextMessage(): " + message);
        log.info("session: " + session.getPrincipal());
        session.sendMessage(message);

        //JSONObject jsonObject = new JSONObject(message.getPayload());
        //String topic = jsonObject.getString("topic");

        // only SEND_MESSAGE topic is available
//        if (topic == null && !topic.equals("SEND_MESSAGE")) {
//            return;
//        }
//
//        if (topic.equals("SEND_MESSAGE")){
//
//        } else if (topic.equals("SEND_MESSAGE")){
//
//        } else if (topic.equals("SEND_MESSAGE")){
//
//        } else if (topic.equals("SEND_MESSAGE")){
//
//        } else if (topic.equals("SEND_MESSAGE")){
//
//        } else if (topic.equals("SEND_MESSAGE")){
//
//        } else if (topic.equals("SEND_MESSAGE")){
//
//        } else if (topic.equals("SEND_MESSAGE")){
//
//        } else if (topic.equals("SEND_MESSAGE")){
//
//        } else if (topic.equals("SEND_MESSAGE")){
//
//        } else if (topic.equals("SEND_MESSAGE")){
//
//        } else if (topic.equals("SEND_MESSAGE")){
//
//        } else if (topic.equals("SEND_MESSAGE")){
//
//        } else if (topic.equals("SEND_MESSAGE")){
//
//        }
        //sender.send(topic, message.getPayload());

    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
        log.error("Websocket transport error: " + exception.getMessage());
    }


};
