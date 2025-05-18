package com.amin.ameenserver.car;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@ToString
public class CarColorDto implements Serializable {
    private final Integer id;
    private final String color;
    private final String colorAr;
}
