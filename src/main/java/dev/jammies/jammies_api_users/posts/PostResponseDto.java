package dev.jammies.jammies_api_users.posts;

import dev.jammies.jammies_api_users.tracks.TrackResponse;
import dev.jammies.jammies_api_users.users.UserResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class PostResponseDto {
    private UUID id;
    private String content;
    private String image;
    private UserResponseDto posted_by;
    private TrackResponse track;
    private Instant created_at;
}
