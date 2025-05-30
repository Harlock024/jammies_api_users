package dev.jammies.jammies_api_users.FavoriteTracks;

import dev.jammies.jammies_api_users.tracks.Track;
import dev.jammies.jammies_api_users.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FavoriteTracksRepository extends JpaRepository<FavoriteTracks, UUID> {
    List<FavoriteTracks> findByUserId(UUID user_id);
    Boolean existsByUserAndTrack(User user, Track track);
    void deleteByUserAndTrack(User user, Track track);


    UUID user(User user);
}
