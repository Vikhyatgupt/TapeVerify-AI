package com.tapeverify.copilot.security;

import com.tapeverify.copilot.entity.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
  private final SecretKey key;
  private final long minutes;

  public JwtService(@Value("${app.jwt.secret:}") String configuredSecret,
                    @Value("${app.jwt.expiration-minutes:480}") long minutes) {
    if (configuredSecret == null || configuredSecret.isBlank()) {
      throw new IllegalStateException("JWT_SECRET must be configured");
    }
    this.key = Keys.hmacShaKeyFor(secretBytes(configuredSecret.trim()));
    this.minutes = minutes;
  }

  public String token(AppUser user) {
    return Jwts.builder().subject(user.getUsername()).claim("role", user.getRole().name())
        .issuedAt(new Date()).expiration(Date.from(Instant.now().plus(Duration.ofMinutes(minutes))))
        .signWith(key).compact();
  }

  public Claims parse(String token) {
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
  }

  private byte[] secretBytes(String value) {
    byte[] bytes;
    try {
      bytes = Base64.getDecoder().decode(value);
    } catch (IllegalArgumentException ignored) {
      bytes = value.getBytes(StandardCharsets.UTF_8);
    }
    if (bytes.length < 32) {
      throw new IllegalStateException("JWT_SECRET must decode to at least 32 bytes");
    }
    return bytes;
  }
}
