package com.gideonisa.security.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.WeakKeyException;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET_KEY = "3778214125432A462D4A614E645267556B58703273357638792F423F4528472B";

    /**
     * Extracts username
     * @param token
     * @return
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracting the claims from the request
     * @param token
     * @param claimsResolver
     * @return
     * @param <T>
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all the content of the claims
     * @param token
     * @return
     */
    private Claims extractAllClaims(String token) {
        return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
            
    }

    /**
     * Creates a new SecretKey instance for use with HMAC-SHA algorithms based on the specified key byte array
     * @return
     */
    private Key getSignInKey() {
        byte[] keyBytes = new byte[0];
        try {
            keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        } catch (Exception e) {
            // TODO: handle exception
            e.getMessage();

        }
            // creates a new secret key based on the []keyBytes decoded
            return Keys.hmacShaKeyFor(keyBytes);
    }

        // obtaining token without claims
        public String generateToken(UserDetails userDetails) {
            return generateToken(new HashMap<>(), userDetails);
        }

    /**
     * Generating a token
     * where we build what the payload should contain
     * @param extractClaims
     * @param userDetails
     * @return
     */
    public String generateToken(Map<String, Object> extractClaims, UserDetails userDetails){
           return Jwts
           .builder()
           .setClaims(extractClaims)
           .setSubject(userDetails.getUsername())
           .setIssuedAt(new Date(System.currentTimeMillis()))
           .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
           .signWith(getSignInKey(), SignatureAlgorithm.HS256)
           .compact();
        }
    
        public Boolean isTokenValid(String token, UserDetails userDetails) {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        }
    
        private boolean isTokenExpired(String token) {
            return extractExpiration(token).before(new Date());
        }
    
        private Date extractExpiration(String token) {
            return extractClaim(token, Claims::getExpiration);
        }


}
