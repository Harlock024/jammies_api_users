package dev.jammies.jammies_api_users.playlist;

import dev.jammies.jammies_api_users.tracks.TrackResponse;
import dev.jammies.jammies_api_users.users.User;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/playlist")
public class PlaylistControllers {

    private final PlaylistService playlistService;

    public PlaylistControllers(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping
    public ResponseEntity<List<PlaylistResponseDto>> getPlaylists() {
        return new ResponseEntity<>(
            playlistService.getAllPlaylists(),
            HttpStatus.OK
        );
    }

    @GetMapping("/user")
    public ResponseEntity<List<PlaylistResponseDto>> getUserPlaylists(
        Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(
            playlistService.getUserPlaylists(user.getId()),
            HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<PlaylistResponseDto> createPlaylist(
        @RequestBody CreatePlaylistRequestDto createPlaylistRequestDto,
        Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(
            playlistService.createPlaylist(
                createPlaylistRequestDto.getName(),
                user
            ),
            HttpStatus.OK
        );
    }

    @PutMapping(path = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PlaylistResponseDto> updatePlaylist(
        @RequestPart PlaylistRequestDto playlistRequestDto,
        @PathVariable UUID id,
        Authentication authentication
    ) throws IOException {
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(
            playlistService.updatePlaylist(id, playlistRequestDto),
            HttpStatus.OK
        );
    }

    @DeleteMapping("{id}/")
    public ResponseEntity<Boolean> deletePlaylist(
        @PathVariable UUID id,
        Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(
            playlistService.deletePlaylist(id),
            HttpStatus.NO_CONTENT
        );
    }

    // tracks to playlist

    @PostMapping("{id}/tracks")
    public ResponseEntity<PlaylistResponseDto> addTrackToPlaylist(
        @PathVariable UUID id,
        @RequestBody AddTrackRequestDto dto,
        Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(
            playlistService.AddTrackToPlaylist(id, dto.getTrack_id()),
            HttpStatus.OK
        );
    }

    @GetMapping("{id}/tracks")
    public ResponseEntity<List<TrackResponse>> getTracksInPlaylist(
        @PathVariable UUID id,
        Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(
            playlistService.getTracksInPlaylist(id),
            HttpStatus.OK
        );
    }

    @DeleteMapping("{id}/tracks/{trackId}")
    public ResponseEntity<Void> removeTrackFromPlaylist(
        @PathVariable UUID id,
        @PathVariable UUID trackId
    ) {
        playlistService.removeTrackFromPlaylist(id, trackId);
        return ResponseEntity.noContent().build();
    }
}
