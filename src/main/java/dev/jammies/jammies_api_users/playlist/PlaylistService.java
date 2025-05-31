package dev.jammies.jammies_api_users.playlist;

import dev.jammies.jammies_api_users.tracks.Track;
import dev.jammies.jammies_api_users.tracks.TrackRepository;
import dev.jammies.jammies_api_users.tracks.TrackResponse;
import dev.jammies.jammies_api_users.tracks.TrackResponseMapper;
import dev.jammies.jammies_api_users.users.User;
import dev.jammies.jammies_api_users.utils.CloudinaryService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PlaylistService {

    private final PlaylistsRepository playlistsRepository;
    private final CloudinaryService cloudinaryService;
    private final TrackRepository trackRepository;

    public PlaylistService(
            PlaylistsRepository playlistsRepository,
            CloudinaryService cloudinaryService,
            TrackRepository trackRepository
    ) {

        this.playlistsRepository = playlistsRepository;
        this.cloudinaryService = cloudinaryService;
        this.trackRepository = trackRepository;

    }

    public List<PlaylistResponseDto> getAllPlaylists() {

        return playlistsRepository
                .findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PlaylistResponseDto> getUserPlaylists(User user) {
        return playlistsRepository
                .findAllByUser(user)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PlaylistResponseDto getPlaylist(UUID playlistId) {
        return playlistsRepository
                .findById(playlistId)
                .map(this::convertToDto)
                .orElseThrow(() -> new NoSuchElementException("Playlist not found")
                );

    }

    public PlaylistResponseDto createPlaylist(String name, User user) {
        Playlist playlist = new Playlist();
        playlist.setName(name);
        playlist.setUser(user);
        playlistsRepository.save(playlist);
        return convertToDto(playlist);
    }

    public PlaylistResponseDto updatePlaylist(
            UUID id,
            PlaylistRequestDto playlistRequestDto
    ) throws IOException {
        Playlist playlist = playlistsRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Playlist not found")
                );


        if (playlistRequestDto.getName() != null) {
            playlist.setName(playlistRequestDto.getName());
        }

        if (playlistRequestDto.getDescription() != null) {
            playlist.setDescription(playlistRequestDto.getDescription());
        }

        if (playlistRequestDto.getCover() != null) {

            var coverResult = cloudinaryService.upload(
                    playlistRequestDto.getCover(),
                    "jammies_track/playlist/cover",
                    "image"
            );

            if (coverResult == null) {
                throw new IOException("Failed to upload cover");
            }
            playlist.setCover_url(coverResult.get("secure_url").toString());
        }

        playlistsRepository.save(playlist);
        return convertToDto(playlist);
    }

    public boolean deletePlaylist(UUID id) {
        if (!playlistsRepository.existsById(id)) {
            throw new NoSuchElementException("Playlist not found");
        }
        playlistsRepository.deleteById(id);
        return true;
    }

    public PlaylistResponseDto convertToDto(@NotNull Playlist playlist) {

        return new PlaylistResponseDto(
                playlist.getId(),
                playlist.getName(),
                playlist.getDescription(),
                playlist.getUser() != null
                        ? playlist.getUser().getId()
                        : null,

                playlist.getUser() != null
                        ? playlist.getUser().getUsername()
                        : null,
                playlist.getCover_url()
        );
    }
    // track to playlist

    public PlaylistResponseDto AddTrackToPlaylist(
            UUID playlistId,
            UUID trackId
    ) {
        Playlist playlist = playlistsRepository
                .findById(playlistId)
                .orElseThrow(() -> new NoSuchElementException("Playlist not found")
                );

        Track track = trackRepository
                .findById(trackId)
                .orElseThrow(() -> new NoSuchElementException("Track not found"));


        playlist.getTracks().add(track);
        playlistsRepository.save(playlist);
        return convertToDto(playlist);
    }

    public List<TrackResponse> getTracksInPlaylist(UUID playlistId, User user) {

        Playlist playlist = playlistsRepository
                .findById(playlistId)
                .orElseThrow(() -> new NoSuchElementException("Playlist not found")
                );
        return TrackResponseMapper.toResponseList(playlist.getTracks().stream().toList(), user);
    }

    public void removeTrackFromPlaylist(UUID playlistId, UUID trackId) {
        Playlist playlist = playlistsRepository
                .findById(playlistId)
                .orElseThrow(() -> new NoSuchElementException("Playlist not found")
                );

        Track Track = trackRepository
                .findById(trackId)
                .orElseThrow(() -> new NoSuchElementException("Track not found"));


        playlist.getTracks().remove(Track);
        playlistsRepository.save(playlist);

    }


}

