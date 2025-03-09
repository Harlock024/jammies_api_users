package dev.jammies.jammies_api_users.RefreshToken;

import dev.jammies.jammies_api_users.users.User;
import dev.jammies.jammies_api_users.utils.Jwt;
import dev.jammies.jammies_api_users.utils.TokenHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class RefreshTokenServices {

    private final RefreshTokenRepository refreshTokenRepository;
    private final Jwt jwtService;
    private final TokenHasher tokenHasher = new TokenHasher();

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpirationMs;


    public void createRefreshToken(User user, String refreshToken, String jti ) {
        RefreshToken token = new RefreshToken();

        token.setId(UUID.fromString(jti));
        token.setUser(user);
        token.setTokenHash(tokenHasher.hashToken(refreshToken));
        token.setExpiresAt(Instant.now().plusMillis(refreshTokenExpirationMs)
        );
        refreshTokenRepository.save(token);
    }

    public void revokeRefreshToken(RefreshToken refreshToken) {
        refreshToken.setRevoked(true);
        refreshToken.setRevokedAt(Instant.now());
        refreshTokenRepository.save(refreshToken);
    }

    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }



}
