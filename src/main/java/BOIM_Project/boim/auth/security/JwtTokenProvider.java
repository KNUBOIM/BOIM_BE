package BOIM_Project.boim.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-expiration-ms}")
    private long accessExpMs;

    @Value("${jwt.refresh-expiration-ms}")
    private long refreshExpMs;

    private Key key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createAccessToken(String email, String role) {
        Date now = new Date();
        return Jwts.builder()
            .setSubject(email)
            .claim("role", role)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + accessExpMs))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public String createRefreshToken(String email) {
        Date now = new Date();
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + refreshExpMs))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public Jws<Claims> validateToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token);
    }
}
