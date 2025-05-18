package com.amin.ameenserver.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Phone Or Email Already Exist")
public class PhoneOrEmailAlreadyExist extends RuntimeException {
    public PhoneOrEmailAlreadyExist(String s) {
    }
}
