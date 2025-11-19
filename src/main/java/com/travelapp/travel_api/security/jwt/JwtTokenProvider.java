package com.travelapp.travel_api.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider implements InitializingBean {

    private final UserDetailsService userDetailsService;
    private final String secret;
    private final long validityInSeconds;
    private SecretKey secretKey;

    public JwtTokenProvider(
            UserDetailsService userDetailsService,
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.token-validity-seconds:3600}") long validityInSeconds
    ) {
        this.userDetailsService = userDetailsService;
        this.secret = secret;
        this.validityInSeconds = validityInSeconds;
    }

    @Override
    public void afterPropertiesSet() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(validityInSeconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Jws<Claims> claims = getClaims(token);
        String username = claims.getBody().getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    private Jws<Claims> getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }
}

