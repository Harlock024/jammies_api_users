package dev.jammies.jammies_api_users.posts;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PostDto {
        private String type;
        private String content;
        private UUID track_id;
}
