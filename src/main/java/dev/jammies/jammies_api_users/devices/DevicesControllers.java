package dev.jammies.jammies_api_users.devices;

import dev.jammies.jammies_api_users.users.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/device")
public class DevicesControllers {

    private final DevicesServices devicesServices;

    public DevicesControllers(DevicesServices devicesServices) {
        this.devicesServices = devicesServices;
    }

    @GetMapping()
    public ResponseEntity<List<DeviceResponseDto>> getDevices(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(devicesServices.getActiveDevicesByUser(user), HttpStatus.OK);
    }


    @PostMapping()
    public ResponseEntity<DeviceResponseDto> addDevice(Authentication authentication, @RequestBody DeviceRequestDto deviceRequestDto) {
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(devicesServices.addDevice(deviceRequestDto, user), HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDevice(Authentication authentication,  @PathVariable UUID id) {
        User user = (User) authentication.getPrincipal();
        devicesServices.markDeviceAsActive(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(Authentication authentication, @PathVariable UUID deviceId) {
        User user = (User) authentication.getPrincipal();
        devicesServices.removeDevices(deviceId, user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}