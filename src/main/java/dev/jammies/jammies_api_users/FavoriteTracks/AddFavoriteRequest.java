package dev.jammies.jammies_api_users.FavoriteTracks;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AddFavoriteRequest {
    private UUID track_id;
}
