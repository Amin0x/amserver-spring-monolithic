package com.amin.ameenserver.user;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class UserDriverDto implements Serializable {
    private Long userId;
    private Long carId;
    private String drivingLicNumber;
    private Date drivingLicExpireDate;
    private Date drivingLicIssueDate;
    private String drivingLicImage;
    private String governmentIdNumber;
    private Date governmentIdExpireDate;
    private Date governmentIdIssueDate;
    private String governmentIdImage;
    private Integer rideCount;
    private Integer rideAcceptedCount;
    private Long ridesDone;
    private Integer likes;
    private Integer points;
    private Boolean ac;
    private Boolean wifi;
    private Boolean charger;
    private Boolean water;
    private Boolean smoking;
    private List<UserCarDto> userCarDtoList;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getDrivingLicNumber() {
        return drivingLicNumber;
    }

    public void setDrivingLicNumber(String drivingLicNumber) {
        this.drivingLicNumber = drivingLicNumber;
    }

    public Date getDrivingLicExpireDate() {
        return drivingLicExpireDate;
    }

    public void setDrivingLicExpireDate(Date drivingLicExpireDate) {
        this.drivingLicExpireDate = drivingLicExpireDate;
    }

    public Date getDrivingLicIssueDate() {
        return drivingLicIssueDate;
    }

    public void setDrivingLicIssueDate(Date drivingLicIssueDate) {
        this.drivingLicIssueDate = drivingLicIssueDate;
    }

    public String getDrivingLicImage() {
        return drivingLicImage;
    }

    public void setDrivingLicImage(String drivingLicImage) {
        this.drivingLicImage = drivingLicImage;
    }

    public String getGovernmentIdNumber() {
        return governmentIdNumber;
    }

    public void setGovernmentIdNumber(String governmentIdNumber) {
        this.governmentIdNumber = governmentIdNumber;
    }

    public Date getGovernmentIdExpireDate() {
        return governmentIdExpireDate;
    }

    public void setGovernmentIdExpireDate(Date governmentIdExpireDate) {
        this.governmentIdExpireDate = governmentIdExpireDate;
    }

    public Date getGovernmentIdIssueDate() {
        return governmentIdIssueDate;
    }

    public void setGovernmentIdIssueDate(Date governmentIdIssueDate) {
        this.governmentIdIssueDate = governmentIdIssueDate;
    }

    public String getGovernmentIdImage() {
        return governmentIdImage;
    }

    public void setGovernmentIdImage(String governmentIdImage) {
        this.governmentIdImage = governmentIdImage;
    }

    public Integer getRideCount() {
        return rideCount;
    }

    public void setRideCount(Integer rideCount) {
        this.rideCount = rideCount;
    }

    public Integer getRideAcceptedCount() {
        return rideAcceptedCount;
    }

    public void setRideAcceptedCount(Integer rideAcceptedCount) {
        this.rideAcceptedCount = rideAcceptedCount;
    }

    public Long getRidesDone() {
        return ridesDone;
    }

    public void setRidesDone(Long ridesDone) {
        this.ridesDone = ridesDone;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Boolean getAc() {
        return ac;
    }

    public void setAc(Boolean ac) {
        this.ac = ac;
    }

    public Boolean getWifi() {
        return wifi;
    }

    public void setWifi(Boolean wifi) {
        this.wifi = wifi;
    }

    public Boolean getCharger() {
        return charger;
    }

    public void setCharger(Boolean charger) {
        this.charger = charger;
    }

    public Boolean getWater() {
        return water;
    }

    public void setWater(Boolean water) {
        this.water = water;
    }

    public Boolean getSmoking() {
        return smoking;
    }

    public void setSmoking(Boolean smoking) {
        this.smoking = smoking;
    }

    public List<UserCarDto> getUserCarDtoList() {
        return userCarDtoList;
    }

    public void setUserCarDtoList(List<UserCarDto> userCarDtoList) {
        this.userCarDtoList = userCarDtoList;
    }
}
