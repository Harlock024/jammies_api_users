package dev.jammies.jammies_api_users.playlist;

import dev.jammies.jammies_api_users.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PlaylistsRepository extends JpaRepository<Playlist, UUID> {
    public List<Playlist> findAllByUser(User user);

}