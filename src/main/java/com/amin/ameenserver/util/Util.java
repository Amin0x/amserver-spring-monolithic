package com.amin.ameenserver.util;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Random;

public class Util {

    public static int generateRandomInt(int upperRange){
        Random random = new Random();
        return random.nextInt(upperRange);
    }

    public static boolean hasRole (String roleName)
    {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(roleName));
    }


}
