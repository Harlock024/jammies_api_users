package dev.jammies.jammies_api_users.playlist;


import java.util.UUID;

public class PlaylistResponseDto {

    public UUID id;
    public String name;
    public String description;
    public String created_by;
    public String cover_url;

    public PlaylistResponseDto(UUID id, String name, String description, String created_by, String cover_url) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.created_by = created_by;
        this.cover_url = cover_url;
    }
}

