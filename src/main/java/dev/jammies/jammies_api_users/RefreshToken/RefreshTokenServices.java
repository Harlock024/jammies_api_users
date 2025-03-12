package dev.jammies.jammies_api_users.RefreshToken;

import dev.jammies.jammies_api_users.users.User;
import dev.jammies.jammies_api_users.utils.Jwt;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class RefreshTokenServices {

    private final RefreshTokenRepository refreshTokenRepository;
    private final Jwt jwtService;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpirationMs;

    public void createRefreshToken(User user, String refreshToken, String jti) {
        RefreshToken token = new RefreshToken();
        String tokenHash = passwordEncoder.encode(refreshToken);

        token.setId(UUID.fromString(jti));
        token.setUser(user);
        token.setTokenHash(tokenHash);
        token.setExpiresAt(Instant.now().plusMillis(refreshTokenExpirationMs));
        refreshTokenRepository.save(token);
    }

    public Optional<RefreshToken> findByUser(User user) {
        return refreshTokenRepository.findByUser(user);
    }

    public Optional<RefreshToken> findByToken(String refreshToken) {
        return refreshTokenRepository.findAll()
                .stream()
                .filter(token -> passwordEncoder.matches(refreshToken, token.getTokenHash()))
                .findFirst();
    }

    @Transactional
    public TokensResponse refreshToken(String refreshToken) {
        Optional<RefreshToken> storedTokenOpt = findByToken(refreshToken);

        if (storedTokenOpt.isEmpty()) {
            return new TokensResponse(null, null, false);
        }

        RefreshToken storedToken = storedTokenOpt.get();

        if (storedToken.getRevoked() || storedToken.getExpiresAt().isBefore(Instant.now())) {
            revokeRefreshToken(storedToken);
            return new TokensResponse(null, null, false);
        }

        User user = storedToken.getUser();
        String newJti = UUID.randomUUID().toString();
        TokensResponse tokensResponse = jwtService.generateTokens(user, newJti);

        deleteByTokenHash(storedToken.getTokenHash());
        createRefreshToken(user, tokensResponse.getRefreshToken(), newJti);

        return tokensResponse;
    }

    public void revokeRefreshToken(RefreshToken refreshToken) {
        refreshToken.setRevoked(true);
        refreshToken.setRevokedAt(Instant.now());
        refreshTokenRepository.save(refreshToken);
    }
    @Transactional
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
    @Transactional
    public void deleteByTokenHash(String tokenHash) {
        refreshTokenRepository.deleteByTokenHash(tokenHash);
    }
}

