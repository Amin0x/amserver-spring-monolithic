package com.amin.ameenserver.car;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "car_manufacurer")
public class CarManufacurer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT UNSIGNED not null")
    private Long id;

    @Column(name = "manufacurer", nullable = false, length = 50)
    private String manufacurer;

    @Column(name = "manufacurer_ar", nullable = false, length = 50)
    private String manufacurerAr;

    public CarManufacurer(String manufacurer, String manufacurerAr) {

        this.manufacurer = manufacurer;
        this.manufacurerAr = manufacurerAr;
    }

    public CarManufacurer() {
    }
}