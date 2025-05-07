package dev.jammies.jammies_api_users.tracks;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UploadTrackRequest {

    private String title;
    private MultipartFile audio;
    private MultipartFile cover;
}
