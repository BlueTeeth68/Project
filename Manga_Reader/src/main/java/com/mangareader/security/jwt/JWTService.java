package com.mangareader.security.jwt;

import com.mangareader.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

//    private static final String SECRECT_KEY = "413F4428472B4B6250655368566D597133743677397A244326462948404D6351";

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    //create a secret key with HMAC-SHA algorithms
    private Key getSigningKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(SECRECT_KEY);
//        return Keys.hmacShaKeyFor(keyBytes);
        return key;
    }

    //get all information of claim
    public Claims extractAllClaim(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //Get Information from a claim
    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaim(token);
        return claimResolver.apply(claims);
    }

    //get email from claims
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //Create token with no Claims
    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }

    //create token
    public String generateToken(
            Map<String, Object> extraClaims,
            User user
    ) {
        /*
        * If I want to add others information to token
        * just put information to the extraClaims like the following
        extraClaims.put("role", userDetails.getAuthorities());
        extraClaims.put("email", userDetails.getPassword());
        */
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractUsername(token);
        return (email.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //To retrieve information from token, we can use JWT library:
    /*
    public Map<String, Object> extractClaims(String token) {
        Claims claims = Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(token).getBody();
        Map<String, Object> extractedClaims = new HashMap<>();
        extractedClaims.put("subject", claims.getSubject());
        extractedClaims.put("role", claims.get("role", String.class));
        extractedClaims.put("email", claims.get("email", String.class));
        return extractedClaims;
    }
    */

}
