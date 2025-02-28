package util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;


import java.security.Key;
import java.util.Date;
import java.util.function.Function;

public class JwtUtil {
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Generates a secure key

    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)  // Payload (user identifier)
                .setIssuedAt(new Date()) // Issued time
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Expiry (1 hour)
                .signWith(SECRET_KEY) // Signing key
                .compact();
    }

    public static void parseToken(String token) {
        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(JwtUtil.SECRET_KEY)
                .build()
                .parseClaimsJws(token);

        System.out.println("Username: " + claims.getBody().getSubject());
        System.out.println("Expiration: " + claims.getBody().getExpiration());
    }

    public static boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)  // Validate the signature
                    .build()
                    .parseClaimsJws(token); // Parse the token

            System.out.println("Token is valid.");
            System.out.println("Username: " + claims.getBody().getSubject());
            System.out.println("Expiration: " + claims.getBody().getExpiration());

            return true;
        } catch (Exception e) {
            System.out.println("Invalid Token Error: " + e.getMessage());
            return false;
        }
    }

}
