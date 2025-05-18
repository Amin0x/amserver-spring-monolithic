package com.amin.ameenserver.car;

import javax.persistence.*;

@Entity
@Table(name = "car_colors")
public class CarColor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "color", nullable = false, length = 100)
    private String color;

    @Column(name = "color_ar", nullable = false, length = 100)
    private String colorAr;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColorAr() {
        return colorAr;
    }

    public void setColorAr(String colorAr) {
        this.colorAr = colorAr;
    }

    public CarColor(String color, String colorAr) {
        this.color = color;
        this.colorAr = colorAr;
    }

    public CarColor() {
    }
}