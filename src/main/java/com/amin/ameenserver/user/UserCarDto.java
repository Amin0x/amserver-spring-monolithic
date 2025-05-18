package com.amin.ameenserver.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class UserCarDto implements Serializable {
    private String manufacturer;
    private String brand;
    private String plate;
    private String year;
    private String color;
}
