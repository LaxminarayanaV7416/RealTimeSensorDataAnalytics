package com.psd.RealTimeSensorDataAnalyticsBackend.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenUtil {

    private final Key secreteKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final long EXPIRATION_TIME = 60 * 60000; // 60 * 1 min = 1 hour

    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secreteKey)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                            .setSigningKey(secreteKey)
                            .build()
                            .parseClaimsJws(token)
                            .getBody();
        String compossedString = claims.getSubject(); // This retrieves the username from the token
        String[] valuesSensitive = compossedString.split(" <> ");
        return valuesSensitive[0];
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secreteKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            // Token is expired
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            // Token is invalid (failed parsing or verification)
            return false;
        }
    }
}