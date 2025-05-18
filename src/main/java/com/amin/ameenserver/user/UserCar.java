package com.amin.ameenserver.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "user_cars")
public class UserCar implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_driver", nullable = false)
    private Long userDriver;

    @Column(name = "brand_id", columnDefinition = "INT UNSIGNED not null")
    private Long brandId;

    @Column(name = "class_id")
    private Integer classId;

    @Column(name = "year", nullable = false)
    private LocalDate year;

    @Column(name = "plate_number", nullable = false, length = 100)
    private String plateNumber;

    @Column(name = "engin_number", length = 100)
    private String enginNumber;

    @Column(name = "color_id", columnDefinition = "INT UNSIGNED not null")
    private Long colorId;

    @Column(name = "reg_number", nullable = false, length = 100)
    private String regNumber;

    @Column(name = "reg_expire", nullable = false)
    private Instant regExpire;

    @Column(name = "reg_name", nullable = false)
    private String regName;

    @Column(name = "reg_name_ar", length = 100)
    private String regNameAr;

    @Column(name = "reg_phone", length = 50)
    private String regPhone;

    @Column(name = "created", nullable = false)
    private Instant created;

    @Column(name = "updated", nullable = false)
    private Instant updated;

    public UserCar(Long userDriver, Long brandId, Integer classId, LocalDate year, String plateNumber, Long colorId,
                   String regNumber, Instant regExpire, String regName, String regNameAr, String regPhone) {
        this.userDriver = userDriver;
        this.brandId = brandId;
        this.classId = classId;
        this.year = year;
        this.plateNumber = plateNumber;
        this.colorId = colorId;
        this.regNumber = regNumber;
        this.regExpire = regExpire;
        this.regName = regName;
        this.regNameAr = regNameAr;
        this.regPhone = regPhone;
    }

    public UserCar() {
    }
}
