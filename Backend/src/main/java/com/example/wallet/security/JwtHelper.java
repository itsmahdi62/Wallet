package com.example.wallet.security;

import com.example.wallet.entity.Person;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
@Slf4j
public class JwtHelper {
    public static final long JWT_TOKEN_VALIDITY = 1000 * 60*60;
    private String secret="09391395538Amir!09391395538Amir!";
    //generate token for person
    public String generateToken(Person person) {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(claims, person.getNationalId());
    }

    private String generateToken(Map<String, Object> claims, String subject) {
        // Convert the secret string key into a Key object
        Key hmacKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(hmacKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token, Person person) {
        final String nationalId = getNationalIdFromToken(token);
        return (nationalId.equals(person.getNationalId()) && !isTokenExpired(token));
    }



    public String getUserNationalIdFromJWTWithoutUsingReq() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        String token = (String) authentication.getPrincipal();
//        log.debug("Extracted token from SecurityContext: {}", token);
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("JWT String argument cannot be null or empty.dddd");
        }
        return this.getNationalIdFromToken(token);
    }

    // retrieve national id from token
    public String getNationalIdFromToken(String token){
        return  getClaimFromToken(token , Claims::getSubject);
    }

    private <T> T getClaimFromToken(String token , Function<Claims ,T> claimResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        Key hmacKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8) , "HmacSHA256");
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
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

}
