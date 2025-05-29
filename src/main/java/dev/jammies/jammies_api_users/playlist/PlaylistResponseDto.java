package dev.jammies.jammies_api_users.playlist;


import java.util.UUID;

public class PlaylistResponseDto {

    public UUID id;
    public String name;
    public String description;
    public  UUID user_id;
    public String created_by;
    public String cover_url;

    public PlaylistResponseDto(UUID id, String name, String description, UUID user_id, String created_by, String cover_url) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.user_id = user_id;
        this.created_by = created_by;
        this.cover_url = cover_url;
    }
}

