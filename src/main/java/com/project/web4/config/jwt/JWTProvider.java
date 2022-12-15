package com.project.web4.config.jwt;

import io.jsonwebtoken.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Log
public class JWTProvider {

    @Value("$(jwt.secret)")
    private String jwtSecret;
    private final long jwtValidity = 3600000;
    private final List<String> userRoles = new ArrayList<>();

    public String generateToken(String username) {
        log.info("Generate token");
        userRoles.add("User");
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", userRoles);
        Date date = new Date();
        Date expTime = new Date(date.getTime() + jwtValidity);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(expTime)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            log.severe("Token expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.severe("Unsupported jwt: " + e.getMessage());
        } catch (MalformedJwtException e) {
            log.severe("Malformed jwt: " + e.getMessage());
        } catch (SignatureException e) {
            log.severe("Invalid signature: " + e.getMessage());
        } catch (Exception e) {
            log.severe("Invalid token: " + e.getMessage());
        }
        return false;
    }

    public String getLoginFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

}
