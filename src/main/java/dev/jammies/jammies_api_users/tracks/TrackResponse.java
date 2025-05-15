package dev.jammies.jammies_api_users.tracks;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TrackResponse {

    public UUID id ;
    public String title;
    public String audio_url;
    public Double duration;
    public String artist;
    public String album;
    public String cover_url;

    public TrackResponse(
        UUID id,
        String title,
        String audio_url,
        Double duration,
        String artist,
        String album,
        String cover_url
    ) {
        this.id = id;
        this.title = title;
        this.audio_url = audio_url;
        this.duration = duration;
        this.artist = artist;
        this.album = album;
        this.cover_url = cover_url;
    }
}
