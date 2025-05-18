package com.amin.ameenserver.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto implements Serializable {
    private String email;
    private String username;
    private String name;
    private String password;
}
