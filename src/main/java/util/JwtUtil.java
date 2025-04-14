package util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jws;

import java.security.Key;
import java.util.Date;


public final class JwtUtil {
    /**
     * The expiration time for the JWT token in milliseconds.
     * This is set to 1 hour (3600000 milliseconds).
     */
    private static final long ONE_HOUR_IN_MILLISECONDS = 3600000;
    /**
     * JWT secret key for signing and verifying tokens.
     * In a real application, this should be stored securely and not hardcoded.
     */
    private static final Key SECRET_KEY = Keys
            .secretKeyFor(SignatureAlgorithm.HS256); // Generates a secure key

    private JwtUtil() {
        throw new UnsupportedOperationException("Utility class");
    }
    /**
     * Generates a JWT token for the given username.
     *
     * @param username The username to include in the token.
     * @return The generated JWT token.
     */
    public static String generateToken(final String username) {
        return Jwts.builder()
                .setSubject(username)  // Payload (user identifier)
                .setIssuedAt(new Date()) // Issued time
                .setExpiration(new Date(System
                        .currentTimeMillis() + ONE_HOUR_IN_MILLISECONDS))
                .signWith(SECRET_KEY) // Signing key
                .compact();
    }

    /**
     * Parses the JWT token and retrieves the claims.
     *
     * @param token The JWT token to parse.
     */
    public static void parseToken(final String token) {
        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(JwtUtil.SECRET_KEY)
                .build()
                .parseClaimsJws(token);

        System.out.println("Username: " + claims.getBody().getSubject());
        System.out.println("Expiration: " + claims.getBody().getExpiration());
    }

    /**
     * Validates the JWT token.
     *
     * @param token The JWT token to validate.
     * @return True if the token is valid, false otherwise.
     */
    public static boolean validateToken(final String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)  // Validate the signature
                    .build()
                    .parseClaimsJws(token); // Parse the token

            System.out.println("Token is valid.");
            System.out.println("Username: " + claims.getBody().getSubject());
            System.out.println("Expires: " + claims.getBody().getExpiration());

            return true;
        } catch (Exception e) {
            System.out.println("Invalid Token Error: " + e.getMessage());
            return false;
        }
    }

}
