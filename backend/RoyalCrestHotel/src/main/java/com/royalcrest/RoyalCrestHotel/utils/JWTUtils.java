package com.royalcrest.RoyalCrestHotel.utils;

import io.jsonwebtoken.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;


//---------------JWTUtils, provides utility methods for handling JSON Web Tokens (JWT) within a Spring application.
// --------------It leverages the io.jsonwebtoken library for creating and validating JWTs.
@Service
public class JWTUtils {
    private static final long EXPIRATION_TIME=1000*60*24*7;
    private final SecretKey Key;
    public JWTUtils(){
        //Can be used to hash
        String secreteString = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
        byte[] keyBytes = Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
        this.Key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }
    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .signWith(Key)
                .compact();
    }


    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }

    //---------extractClaims: This method parses the token to extract the claims using the provided claimsResolver function.
    // --------It uses the secret key to validate the token.
    private <T> T extractClaims(String token, Function<Claims,T> claimsTFunction){
        return claimsTFunction.apply(Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload());
    }

    public boolean isValidToken(String token,UserDetails userDetails){
        final String username=extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    private boolean isTokenExpired(String token){
        return extractClaims(token,Claims::getExpiration).before(new Date());
    }
    public String refreshToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(Key).build().parseClaimsJws(token).getBody();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Key)
                .compact();
    }
}


