package dev.jammies.jammies_api_users.utils;


import dev.jammies.jammies_api_users.RefreshToken.RefreshToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TokenHasher {

    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

    public String hashToken(String refreshToken) {
        return bcrypt.encode(refreshToken);
    }

    public boolean verifyToken(String rawToken, String refreshToken) {
        return bcrypt.matches(refreshToken, rawToken);
    }
}
