package dev.jammies.jammies_api_users.posts;

import dev.jammies.jammies_api_users.users.UserResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class PostResponseDto {
    private UUID id;
    private String content;
    private UserResponseDto user;
    private UUID track_id;
    private Instant created_at;


    public PostResponseDto(UUID id, String content, UserResponseDto user, UUID track_id, Instant created_at) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.track_id = track_id;
        this.created_at = created_at;
    }

    public PostResponseDto(UUID id, String content, UserResponseDto user, Instant created_at) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.created_at = created_at;
    }
}
