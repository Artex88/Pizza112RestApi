package ru.urfu.pizzaSite.RestApiPizzaApplication.security;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;
@Component
public class TelegramTokenGenerator {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
