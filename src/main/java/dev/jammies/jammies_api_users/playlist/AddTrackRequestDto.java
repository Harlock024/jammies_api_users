package dev.jammies.jammies_api_users.playlist;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AddTrackRequestDto {
    private UUID track_id;
}
