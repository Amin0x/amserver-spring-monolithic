package com.amin.ameenserver.car;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@ToString
public class CarBrandDto implements Serializable {
    private final Integer id;
    private final String brandAr;
    private final Integer manufactureId;
}
