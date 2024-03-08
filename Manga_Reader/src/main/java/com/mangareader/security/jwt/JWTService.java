package com.mangareader.security.jwt;

import com.mangareader.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${jwt.accessKey}")
    private String accessKey;

    @Value("${jwt.refreshKey}")
    private String refreshKey;

    //create a secret key with HMAC-SHA algorithms
    private Key getSignInKey(String key) {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //get information of all Claims
    private Claims extractAllClaim(String token, String key) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey(key))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //Get Information from a claim
    public <T> T extractClaim(String token, Function<Claims, T> claimResolver, String key) {
        final Claims claims = extractAllClaim(token, key);
        return claimResolver.apply(claims);
    }

    //get object from claims
    public String extractAccessUserName(String token) {
        return extractClaim(token, Claims::getSubject, accessKey);
    }

    public String extractRefreshUserName(String token) {
        return extractClaim(token, Claims::getSubject, refreshKey);
    }

    public Date extractExpiration(String token, String key) {
        return extractClaim(token, Claims::getExpiration, key);
    }

    //create access token
    public String generateAccessToken(
            User user
    ) {
        Map<String, String> claim = new HashMap<>();
        claim.put("role", user.getRole().name());
        claim.put("sub", user.getUsername());

        return Jwts
                .builder()
                .setClaims(claim)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 ))
                .signWith(getSignInKey(accessKey), SignatureAlgorithm.HS256)
                .compact();
    }

    //create refresh token
    public String generateRefreshToken(
            User user
    ) {

        return Jwts
                .builder()
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .signWith(getSignInKey(refreshKey), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isAccessTokenValid(String compactJws) {
        try {
            //check if the token is legally generated
            Jwts.parserBuilder().setSigningKey(getSignInKey(accessKey)).build().parseClaimsJws(compactJws);
        } catch (JwtException e) {
            return false;
        }
        return (!isTokenExpired(compactJws, accessKey));
    }

    public boolean isRefreshTokenValid(String compactJws) {
        try {
            //check if the token is legally generated
            Jwts.parserBuilder().setSigningKey(getSignInKey(refreshKey)).build().parseClaimsJws(compactJws);
        } catch (JwtException e) {
            return false;
        }
        return (!isTokenExpired(compactJws, refreshKey));
    }

    public boolean isTokenExpired(String token, String key) {
        return extractExpiration(token, key).before(new Date());
    }

}
