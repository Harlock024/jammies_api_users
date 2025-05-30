package dev.jammies.jammies_api_users.FavoriteTracks;

import dev.jammies.jammies_api_users.tracks.Track;
import dev.jammies.jammies_api_users.tracks.TrackRepository;
import dev.jammies.jammies_api_users.tracks.TrackResponse;
import dev.jammies.jammies_api_users.users.User;
import dev.jammies.jammies_api_users.users.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FavoriteTracksServices {

    private final FavoriteTracksRepository favoriteTracksRepository;
    private final TrackRepository trackRepository;
    private final UsersRepository usersRepository;

    public FavoriteTracksServices(FavoriteTracksRepository favoriteTracksRepository, TrackRepository trackRepository, UsersRepository usersRepository) {
        this.favoriteTracksRepository = favoriteTracksRepository;
        this.trackRepository = trackRepository;
        this.usersRepository = usersRepository;
    }

    public List<TrackResponse> getFavoriteTracksByUser(UUID user_id) {
        List<Track> tracks = favoriteTracksRepository.findByUserId(user_id).stream()
                .map(FavoriteTracks::getTrack)
                .toList();
        List<TrackResponse> trackResponses = new ArrayList<>();

        for (Track track : tracks) {
            trackResponses.add(converTrackToDto(track));
        }
        return trackResponses;
    }

    public TrackResponse addTrackToFavorite(User user, UUID trackId) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new RuntimeException("Track not found"));

        boolean alreadyExists = favoriteTracksRepository.existsByUserAndTrack(user, track);
        if (alreadyExists) {
            throw new RuntimeException("Track is already in favorites");
        }

        FavoriteTracks favoriteTracks = new FavoriteTracks();
        favoriteTracks.setTrack(track);
        favoriteTracks.setUser(user);
        favoriteTracksRepository.save(favoriteTracks);

        return converTrackToDto(track);
    }


    public TrackResponse converTrackToDto(Track track) {
        return new TrackResponse(
                track.getId(),
                track.getTitle(),
                track.getDuration(),
                track.getUser().getUsername(),
                track.getTitle(),
                track.getCover_image()
        );
    }
    @Transactional
    public void removeTrackFromFavorite(User user, UUID trackId) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new RuntimeException("Track not found"));

        boolean exists = favoriteTracksRepository.existsByUserAndTrack(user, track);

        if (!exists) {
            throw new RuntimeException("Track is not in favorites");
        }
        favoriteTracksRepository.deleteByUserAndTrack(user, track);
    }
}

