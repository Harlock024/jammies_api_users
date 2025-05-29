package dev.jammies.jammies_api_users.playlist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlaylistsRepository extends JpaRepository<Playlist, UUID> {

}