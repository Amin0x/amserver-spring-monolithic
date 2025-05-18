package com.amin.ameenserver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyWebSocketMessage {
    private String to;
    private String from;
    private long messageId;
    private int messageType;
    private String payload;
}
