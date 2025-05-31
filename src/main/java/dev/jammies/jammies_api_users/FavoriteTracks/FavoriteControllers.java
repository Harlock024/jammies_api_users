package dev.jammies.jammies_api_users.FavoriteTracks;


import dev.jammies.jammies_api_users.tracks.TrackResponse;
import dev.jammies.jammies_api_users.users.User;
import dev.jammies.jammies_api_users.users.UsersRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/favorite_tracks")
public class FavoriteControllers {

    private final FavoriteTracksServices favoriteTracksServices;
    private final UsersRepository usersRepository;

    public FavoriteControllers(FavoriteTracksServices favoriteTracksServices, UsersRepository usersRepository) {
        this.favoriteTracksServices = favoriteTracksServices;
        this.usersRepository = usersRepository;
    }

    @GetMapping()
    @Transactional
    public ResponseEntity<List<TrackResponse>> getFavoriteTracks(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        User managedUser = usersRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return new ResponseEntity<>(favoriteTracksServices.getFavoriteTracksByUser(managedUser), HttpStatus.OK);

    }

    @PostMapping()
    @Transactional
    public ResponseEntity<TrackResponse> addFavoriteTrack(Authentication authentication, @RequestBody AddFavoriteRequest request) {
        User user = (User) authentication.getPrincipal();
        User managedUser = usersRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return new ResponseEntity<>(favoriteTracksServices.addTrackToFavorite(managedUser, request.getTrack_id()), HttpStatus.OK);
    }

    @DeleteMapping("/{track_id}")
    @Transactional
    public ResponseEntity<?> removeFavoriteTrack(Authentication authentication, @PathVariable UUID track_id) {
        User user = (User) authentication.getPrincipal();

        User managedUser = usersRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        favoriteTracksServices.removeTrackFromFavorite(managedUser, track_id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
