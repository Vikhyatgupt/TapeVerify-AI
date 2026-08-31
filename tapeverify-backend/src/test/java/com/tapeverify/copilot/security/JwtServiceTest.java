package com.tapeverify.copilot.security;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Base64;
import org.junit.jupiter.api.Test;

class JwtServiceTest {
  @Test
  void requiresAConfiguredSecretWithAtLeastThirtyTwoBytes() {
    assertThrows(IllegalStateException.class, () -> new JwtService("", 30));
    assertThrows(IllegalStateException.class, () -> new JwtService("too-short", 30));
    String secret = Base64.getEncoder().encodeToString(new byte[32]);
    assertDoesNotThrow(() -> new JwtService(secret, 30));
  }
}
