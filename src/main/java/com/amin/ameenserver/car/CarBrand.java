package com.amin.ameenserver.car;

import javax.persistence.*;

@Entity
@Table(name = "car_brands")
public class CarBrand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "brand_ar", nullable = false, length = 100)
    private String brandAr;

    @Column(name = "manufacture_id", nullable = false)
    private Integer manufactureId;

    public Integer getManufactureId() {
        return manufactureId;
    }

    public void setManufactureId(Integer manufactureId) {
        this.manufactureId = manufactureId;
    }

    public String getBrandAr() {
        return brandAr;
    }

    public void setBrandAr(String brandAr) {
        this.brandAr = brandAr;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CarBrand() {
    }

    public CarBrand(String brandAr, Integer manufactureId) {
        this.brandAr = brandAr;
        this.manufactureId = manufactureId;
    }
}