package com.example.wallet.security;

import com.example.wallet.entity.Person;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtHelper {
    public static final long JWT_TOKEN_VALIDITY = 5 * 60*60;
    private String secret="Your secret key 09391395538";

    // retrieve username
    public String getNationalIdFromToken(String token){
        return  getClaimFromToken(token , Claims::getSubject);
    }
    
    private <T> T getClaimFromToken(String token , Function<Claims ,T> claimResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        Key hmacKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8) , "HacSHA256");
        return Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(Person person) {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(claims, person.getNationalId());
    }

    private String generateToken(Map<String, Object> claims, String subject) {
        // Convert the secret string key into a Key object
        Key hmacKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.HS512.getJcaName());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(hmacKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public Boolean validateToken(String token, Person person) {
        final String nationalId = getNationalIdFromToken(token);
        return (person.equals(person.getNationalId()) && !isTokenExpired(token));
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

}
