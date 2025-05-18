package com.amin.ameenserver.auth;

import com.amin.ameenserver.user.UserDto;

public class LoginResponse {
    private String token;
    private UserDto userDto;

    public LoginResponse(String token, UserDto userDto) {
        this.token = token;
        this.userDto = userDto;
    }

    public LoginResponse() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }
}