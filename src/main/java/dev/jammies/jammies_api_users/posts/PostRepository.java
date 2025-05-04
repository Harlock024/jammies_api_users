package dev.jammies.jammies_api_users.posts;

import dev.jammies.jammies_api_users.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    Optional<List<Post>> findByUserId(UUID id);
        @Query("SELECT p FROM Post p WHERE (:cursor IS NULL OR p.createdAt < :cursor) ORDER BY p.createdAt DESC")
    List<Post> findPostsByCursor(
                @Param("cursor") Instant cursor,
                Pageable pageable
        );
}
