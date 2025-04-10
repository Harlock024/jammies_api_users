package dev.jammies.jammies_api_users.tracks;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TrackRepository  extends JpaRepository<Track, UUID> {
    public Track findByTitle(String title);
    public List<Track> findByUserId(UUID userId);

}
