package com.ania.cookbook.application.properties;

//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import java.security.Key;
//import java.util.Base64;
//import java.util.Date;


public class JwtUtil {
//    private static final String SECRET_KEY_BASE64 = "bXlzZWNyZXRrZXlteXNlY3JldGtleW15c2VjcmV0a2V5";
//    private static final long EXPIRATION_TIME = 86400000L;
//
//    private static Key getSigningKey() {
//        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY_BASE64);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    private static Date getCurrentDate() {
//        return new Date();
//    }
//
//    private static Date calculateExpirationDate() {
//        return new Date(System.currentTimeMillis() + EXPIRATION_TIME);
//    }
//
//    public static String createJwtToken(String username) {
//        return Jwts.builder()
//                .subject(username)
//                .issuedAt(getCurrentDate())
//                .expiration(calculateExpirationDate())
//                .signWith(getSigningKey())
//                .compact();
//    }
//
//    public static Claims parseToken(String token) {
//        return Jwts.parser()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
}
