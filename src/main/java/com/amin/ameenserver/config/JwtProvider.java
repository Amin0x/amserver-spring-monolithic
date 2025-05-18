package com.amin.ameenserver;

import com.amin.ameenserver.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtProvider {
    private final String jwtSecret = "ameenapp123@ameenapp123@ameenapp123@ameenapp123@ameenapp123@ameenapp123@ameenapp123";
    private final int jwtExpirationInMs = 86400000; // 10 * 60 * 60 * 1000;

    public String generateAccessToken(User authentication) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(authentication.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUserUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecret.getBytes()).build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecret.getBytes()).build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (JwtException ex) {
            log.error("token validation error", ex);
        }
        return false;
    }

}
