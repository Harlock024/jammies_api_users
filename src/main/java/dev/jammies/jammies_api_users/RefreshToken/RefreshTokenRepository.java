package dev.jammies.jammies_api_users.RefreshToken;

import dev.jammies.jammies_api_users.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);
    Optional<RefreshToken> findByUser(User user);
    void deleteByUser(User user);
    void deleteByTokenHash(String tokenHash);
}