package dev.jammies.jammies_api_users.tracks;


import dev.jammies.jammies_api_users.users.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

public class TrackResponseMapper {



    public static TrackResponse toResponse(Track track, Set<UUID> favoriteIds) {
        return new TrackResponse(
            track.getId(),
            track.getTitle(),
            track.getDuration(),
            track.getUser().getUsername(),
            track.getTitle(),
            track.getCover_image(),
            favoriteIds.contains(track.getId())
        );
    }

   public static TrackResponse toResponse(Track track, User user) {

    Set<UUID> favoriteIds = user.getFavorite_tracks().stream()
        .map(fav -> fav.getTrack().getId())
        .collect(Collectors.toSet());

    boolean isFavorite = favoriteIds.contains(track.getId());

    return new TrackResponse(
        track.getId(),
        track.getTitle(),
        track.getDuration(),
        track.getUser().getUsername(),
        track.getTitle(),
        track.getCover_image(),
        isFavorite
    );
}



    public static List<TrackResponse> toResponseList(List<Track> tracks, User user) {
        Set<UUID> favoriteIds = user.getFavorite_tracks().stream()
            .map(fav ->fav.getTrack().getId())
            .collect(Collectors.toSet());

        return tracks.stream()
            .map(track -> toResponse(track, favoriteIds))
            .collect(Collectors.toList());
    }
}
