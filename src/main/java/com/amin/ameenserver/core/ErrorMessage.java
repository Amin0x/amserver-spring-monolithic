package com.amin.ameenserver.core;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessage {
    private String message;
    private int code;
}
