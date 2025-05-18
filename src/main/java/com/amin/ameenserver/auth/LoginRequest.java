package com.amin.ameenserver.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest implements Serializable {
    public String userName;
    public String password;
    public String role;
}
