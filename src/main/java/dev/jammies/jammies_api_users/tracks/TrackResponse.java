package dev.jammies.jammies_api_users.tracks;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TrackResponse {

    public UUID id ;
    public String title;
    public Double duration;
    public String posted_by;
    public String album;
    public String cover_url;
    public Boolean is_favorite;



    public TrackResponse(
        UUID id,
        String title,
        Double duration,
        String artist,
        String album,
        String cover_url,

        Boolean is_favorite

    ) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.posted_by = artist;
        this.album = album;
        this.cover_url = cover_url;
        this.is_favorite = is_favorite;
    }
}
