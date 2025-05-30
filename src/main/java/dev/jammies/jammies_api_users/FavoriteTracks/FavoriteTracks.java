package dev.jammies.jammies_api_users.FavoriteTracks;

import dev.jammies.jammies_api_users.tracks.Track;
import dev.jammies.jammies_api_users.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;


@Getter
@Setter
@Entity
@Table(name = "favorite_tracks")
public class FavoriteTracks implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id", nullable = false)
    private Track track;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
}

