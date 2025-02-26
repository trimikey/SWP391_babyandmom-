package com.swp.BabyandMom.Service;


import com.auth0.jwt.exceptions.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.function.Function;
import com.swp.BabyandMom.Entity.User;
import com.swp.BabyandMom.ExceptionHandler.InvalidToken;
import java.util.Map;
import java.util.HashMap;

@Component
public class JWTService {
    private static final String SECRET = "1vmQU96Qje9RCP24GdxuQJwOavCmQrDcElEQwYo5nFC/emiX8KjSCsn7C4IJQWQz";
    private final long EXPIRATION = 1 * 24 * 60 * 60 * 1000; // Token expiration time (1 day)
    private final long EXPIRATION_REFRESHTOKEN = 7 * 24 * 60 * 60 * 1000; // Refresh token expiration time (7 days)




    // Create the access token
    public String generateToken(String email) {
        Date now = new Date(); // Current time
        Date expirationDate = new Date(now.getTime() + EXPIRATION);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }




    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRoles());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }



    // Create the refresh token
    public String generateRefreshToken(String email) {
        Date now = new Date(); // Current time
        Date expirationDate = new Date(now.getTime() + EXPIRATION_REFRESHTOKEN);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            Instant expiredOn = e.getClaims().getExpiration().toInstant();
            throw new TokenExpiredException("Token has expired", expiredOn);
        } catch (JwtException e) {
            throw new InvalidToken("Invalid token");
        } catch (Exception e) {
            throw new InvalidToken("Error parsing token: " + e.getMessage());
        }
    }



    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    

    public Boolean validateToken(String token, User userDetails) {
        final String userName = extractEmail(token);
        return (userName.equals(userDetails.getEmail()) && !isTokenExpired(token));
    }

}
