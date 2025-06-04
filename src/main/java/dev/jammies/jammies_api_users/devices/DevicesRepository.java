package dev.jammies.jammies_api_users.devices;

import dev.jammies.jammies_api_users.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DevicesRepository extends JpaRepository<Device,UUID> {
    List<Device> findByUserAndActiveTrue(User user);

    Optional<Device> findByIdAndUser(UUID deviceId, User user);
}
