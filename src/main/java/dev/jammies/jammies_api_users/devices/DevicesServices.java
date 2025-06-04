package dev.jammies.jammies_api_users.devices;

import dev.jammies.jammies_api_users.users.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DevicesServices {

    final DevicesRepository devicesRepository;

    public DevicesServices(DevicesRepository devicesRepository) {
        this.devicesRepository = devicesRepository;
    }

    public DeviceResponseDto addDevice(DeviceRequestDto deviceRequestDto, User user) {
        Device device = new Device();
        device.setDevice_name(deviceRequestDto.getDevice_name());
        device.setUser(user);
        return converseToResponseDto(devicesRepository.save(device));
    }

    public List<DeviceResponseDto> getActiveDevicesByUser(User user) {
        return devicesRepository.findByUserAndActiveTrue(user)
                .stream()
                .map(this::converseToResponseDto)
                .collect(Collectors.toList());
    }

    public void markDeviceAsActive(UUID deviceId) {
        devicesRepository.findById(deviceId).ifPresent(device -> {
            device.setActive(true);
            devicesRepository.save(device);
        });
    }

    public void removeDevices(UUID deviceId, User user) {
        Optional<Device> device = devicesRepository.findByIdAndUser(deviceId, user);
        device.ifPresent(devicesRepository::delete);
    }

    private DeviceResponseDto converseToResponseDto(Device device) {
        DeviceResponseDto deviceResponseDto = new DeviceResponseDto();
        deviceResponseDto.setDevice_id(device.getId());
        deviceResponseDto.setDevice_name(device.getDevice_name());

        return deviceResponseDto;
    }
}