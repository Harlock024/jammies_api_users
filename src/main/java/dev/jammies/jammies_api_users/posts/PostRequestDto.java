package dev.jammies.jammies_api_users.posts;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Setter
@Getter
public class PostRequestDto {
    private String content;
    private UUID track_id;
    private MultipartFile image;
}
