package dev.jammies.jammies_api_users.FavoriteTracks;


import dev.jammies.jammies_api_users.tracks.TrackResponse;
import dev.jammies.jammies_api_users.users.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/favorite_tracks")
public class FavoriteControllers {

    private final FavoriteTracksServices favoriteTracksServices;

    public FavoriteControllers(FavoriteTracksServices favoriteTracksServices) {
        this.favoriteTracksServices = favoriteTracksServices;
    }

    @GetMapping()
    public ResponseEntity<List<TrackResponse>> getFavoriteTracks(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(favoriteTracksServices.getFavoriteTracksByUser(user), HttpStatus.OK);

    }

    @PostMapping()
    public ResponseEntity<TrackResponse> addFavoriteTrack(Authentication authentication, @RequestBody AddFavoriteRequest request) {
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(favoriteTracksServices.addTrackToFavorite(user, request.getTrack_id()), HttpStatus.OK);
    }

    @DeleteMapping("/{track_id}")
    public ResponseEntity<?> removeFavoriteTrack(Authentication authentication, @PathVariable UUID track_id) {
        User user = (User) authentication.getPrincipal();

        favoriteTracksServices.removeTrackFromFavorite(user, track_id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
