package com.amin.ameenserver;

import com.amin.ameenserver.location.UpdateUserLocationDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
public class WebSocketService {
    private final List<WebSocketSession> sessionMap = new CopyOnWriteArrayList<>();

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void addWebSocketSession(WebSocketSession session) {
        sessionMap.add(session);
    }

    public void removeWebSocketSession(WebSocketSession session) {
        if (session != null) {
            sessionMap.remove(session);
        }
    }

    public WebSocketSession getSession(String session){
        for (WebSocketSession webSocketSession : sessionMap) {
            Principal principal = webSocketSession.getPrincipal();
            if (principal != null && principal.getName().equals(session)) {
                return webSocketSession;
            }
        }
        return null;
    }

    public void sendBroadcastMessage(MyWebSocketMessage webSocketMessage){

        sessionMap.forEach(webSocketSession -> {
            if (webSocketSession != null && webSocketSession.isOpen()){

                try {
                    webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(webSocketMessage)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void sendMessage(String username, MyWebSocketMessage webSocketMessage){

        try {
            sendMessage(username, objectMapper.writeValueAsString(webSocketMessage));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    public void receiveMessage(TextMessage message){
        try {
            MyWebSocketMessage webSocketMessage = objectMapper.readValue(message.getPayload(), MyWebSocketMessage.class);

            int messageType = webSocketMessage.getMessageType();

            if (messageType == WebSocketConfig.USER_LOCATION){
                UpdateUserLocationDto updateUserLocationDto;
                updateUserLocationDto = objectMapper.readValue(message.getPayload(), UpdateUserLocationDto.class);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String username, String message) {
        log.info("WebSocketService::sendMessage(): username="+username+" message=" + message);
        for (WebSocketSession session : sessionMap) {
            if (session.getPrincipal().getName().equals(username)){
                WebSocketMessage<?> msg = new TextMessage(message);
                try {
                    session.sendMessage(msg);
                } catch (IOException e) {
                    log.error("error websocket send message: ", e);
                }

                break;
            }
        }
    }
}


