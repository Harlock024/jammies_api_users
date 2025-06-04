package dev.jammies.jammies_api_users.devices;

import dev.jammies.jammies_api_users.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@Entity
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String device_name;

    @Column
    private boolean active = true;

    @Column
    private LocalDateTime lastConnectedAt;
}
