package dev.jammies.jammies_api_users.devices;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DeviceResponseDto {
    private String device_name;
    private UUID device_id;
}