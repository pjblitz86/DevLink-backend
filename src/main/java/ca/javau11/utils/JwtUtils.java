package ca.javau11.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import ca.javau11.entities.User;

@Component
public class JwtUtils {
	private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public static String generateToken(User existingUser) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 86400000); // 1 day expiration

        return Jwts.builder()
                .setSubject(existingUser.getEmail()) // Set email (or other identifying info)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    public static Claims validateToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public static String extractUsername(String token) {
        return validateToken(token).getSubject();
    }

    public static boolean isTokenExpired(String token) {
        return validateToken(token).getExpiration().before(new Date());
    }
}
