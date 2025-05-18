package com.amin.ameenserver.location;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "areas")
public class LocationArea implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "location")
    private String location;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "latitude1", nullable = false)
    private Double latitude1;

    @Column(name = "longitude1", nullable = false)
    private Double longitude1;

    @Column(name = "latitude2", nullable = false)
    private Double latitude2;

    @Column(name = "longitude2", nullable = false)
    private Double longitude2;

    @Column(name = "north_area")
    private Integer northArea;

    @Column(name = "east_area")
    private Integer eastArea;

    @Column(name = "west_area")
    private Integer westArea;

    @Column(name = "south_area")
    private Integer southArea;

    public Integer getSouthArea() {
        return southArea;
    }

    public void setSouthArea(Integer southArea) {
        this.southArea = southArea;
    }

    public Integer getWestArea() {
        return westArea;
    }

    public void setWestArea(Integer westArea) {
        this.westArea = westArea;
    }

    public Integer getEastArea() {
        return eastArea;
    }

    public void setEastArea(Integer eastArea) {
        this.eastArea = eastArea;
    }

    public Integer getNorthArea() {
        return northArea;
    }

    public void setNorthArea(Integer northArea) {
        this.northArea = northArea;
    }

    public Double getLongitude2() {
        return longitude2;
    }

    public void setLongitude2(Double longitude2) {
        this.longitude2 = longitude2;
    }

    public Double getLatitude2() {
        return latitude2;
    }

    public void setLatitude2(Double latitude2) {
        this.latitude2 = latitude2;
    }

    public Double getLongitude1() {
        return longitude1;
    }

    public void setLongitude1(Double longitude1) {
        this.longitude1 = longitude1;
    }

    public Double getLatitude1() {
        return latitude1;
    }

    public void setLatitude1(Double latitude1) {
        this.latitude1 = latitude1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
