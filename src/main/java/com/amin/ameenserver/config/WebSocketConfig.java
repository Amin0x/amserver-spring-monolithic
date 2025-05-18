package com.amin.ameenserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.AbstractHandshakeHandler;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocket
//@EnableWebSocketMessageBroker
public class WebSocketConfig implements /*WebSocketMessageBrokerConfigurer,*/ WebSocketConfigurer {

    public static final int ORDER_BID_ACCEPTED = 1;
    public static final int NEW_ORDER = 2;
    public static final int ORDER_STATUS_CHANGED = 3;
    public static final int ORDER_ACCEPT_OTHER = 4;
    public static final int USER_LOCATION = 5;

    @Autowired
    MyWebSocketHandler webSocketHandler;

//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/ws")
//            .setAllowedOriginPatterns("*")
//            .setHandshakeHandler(new DefaultHandshakeHandler() {
//
//            public boolean beforeHandshake(
//                    ServerHttpRequest request,
//                    ServerHttpResponse response,
//                    WebSocketHandler wsHandler,
//                    Map attributes) throws Exception {
//
//                if (request instanceof ServletServerHttpRequest) {
//                    ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
//                    //HttpSession session = servletRequest.getServletRequest().getSession();
//                    //attributes.put("sessionId", session.getId());
//                    if (request.getPrincipal() == null){
//                        return false;
//                    }
//
//                    attributes.put("sessionId", request.getPrincipal().getName());
//
//                }
//                return true;
//            }})/*.withSockJS()*/;
//
//    }
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.setApplicationDestinationPrefixes("/app");
//        //registry.setUserDestinationPrefix("/user");
//    }
//
//    @Override
//    public void configureClientOutboundChannel(ChannelRegistration registration) {
//        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
//        threadPoolTaskExecutor.setCorePoolSize(10);
//        registration.taskExecutor(threadPoolTaskExecutor);
//    }


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/stream")
                .addInterceptors(new MyInterceptor())
                .setHandshakeHandler(new MyHandshakeHandler())
                .setAllowedOriginPatterns("*")
                .setAllowedOrigins("*");
                /*.setAllowedOrigins("http://10.0.2.2:8080", "ws://10.0.2.2:8080",
                        "https://10.0.2.2:8080", "wss://10.0.2.2:8080");*/
    }

    public static class MyInterceptor implements HandshakeInterceptor {

        @Override
        public boolean beforeHandshake(ServerHttpRequest request, org.springframework.http.server.ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
            return true;
        }

        @Override
        public void afterHandshake(ServerHttpRequest request, org.springframework.http.server.ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
            if(request.getHeaders() == null )
                return ;
            if(request.getHeaders().get("Sec-WebSocket-Protocol") == null)
                return ;
            String protocol = (String) request.getHeaders().get("Sec-WebSocket-Protocol").get(0);
            if(protocol == null)
                return ;

            response.getHeaders().add("Sec-WebSocket-Protocol", protocol);
        }
    }

    @Component
    @Slf4j
    public static class MyHandshakeHandler extends AbstractHandshakeHandler {
        @Override
        protected void handleInvalidUpgradeHeader(ServerHttpRequest request, org.springframework.http.server.ServerHttpResponse response) throws IOException {
            super.handleInvalidUpgradeHeader(request, response);
            log.error("handleInvalidUpgradeHeader websocket error on handshake");
        }

        @Override
        protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
            return super.determineUser(request, wsHandler, attributes);
        }
    }
}
