package com.amin.ameenserver.user;

import java.util.List;

public class UserProfileDto {

    private String name;
    private String phoneNumber;
    private String email;
    private double rate;
    private int likes;
    private int points;
    private String image;
    private List<UserCar> cars;

    public UserProfileDto() {
    }

    public UserProfileDto(String name, String phoneNumber, String email, double rate, int likes, int points, String image) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.rate = rate;
        this.likes = likes;
        this.points = points;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
