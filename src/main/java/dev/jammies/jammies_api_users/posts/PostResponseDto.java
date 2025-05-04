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
    private String type;
    private String content;
    private UserResponseDto posted_by;
    private UUID track_id;
    private Instant created_at;

    public PostResponseDto(UUID id,String type ,String content, UserResponseDto posted_by, UUID track_id, Instant created_at) {
        this.id = id;
        this.type = type;
        this.content = content;
        this.posted_by = posted_by;
        this.track_id = track_id;
        this.created_at = created_at;
    }

    public PostResponseDto(UUID id,String type, String content, UserResponseDto posted_by, Instant created_at) {
        this.id = id;
        this.type = type;
        this.content = content;
        this.posted_by = posted_by;
        this.created_at = created_at;
    }
}
