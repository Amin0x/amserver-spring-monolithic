package com.amin.ameenserver.user;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserDto implements Serializable {
    private long id;
    private String phone;
    private String email;
    private String name;
    private String status;
    private boolean enabled;
    private String rememberToken;
    private boolean verified;
    private Date registrationDate;
    private String profileImage;
    private Boolean driverMode;
    private List<String> roles;
    private List<UserCarDto> userCars;
    private UserDriverDto userDriver;
    private UserRiderDto userRider;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRememberToken() {
        return rememberToken;
    }

    public void setRememberToken(String rememberToken) {
        this.rememberToken = rememberToken;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Boolean getDriverMode() {
        return driverMode;
    }

    public void setDriverMode(Boolean driverMode) {
        this.driverMode = driverMode;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<UserCarDto> getUserCars() {
        return userCars;
    }

    public void setUserCars(List<UserCarDto> userCars) {
        this.userCars = userCars;
    }

    public UserDriverDto getUserDriver() {
        return userDriver;
    }

    public void setUserDriver(UserDriverDto userDriver) {
        this.userDriver = userDriver;
    }

    public UserRiderDto getUserRider() {
        return userRider;
    }

    public void setUserRider(UserRiderDto userRider) {
        this.userRider = userRider;
    }
}
