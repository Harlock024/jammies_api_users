package dev.jammies.jammies_api_users.playlist;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PlaylistRequestDto {
    private String name;
    private String description;
    private MultipartFile cover;
}