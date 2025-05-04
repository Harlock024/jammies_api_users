package dev.jammies.jammies_api_users.auth;

import dev.jammies.jammies_api_users.users.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthControllers {

    private final AuthServices authServices;

    public AuthControllers(AuthServices authServices) {
        this.authServices = authServices;
    }

    @CrossOrigin(origins = "http://localhost:4321")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> sign_up(@RequestBody UserDTO newUser) {
        AuthResponse auth = authServices.signup(newUser);
        return new ResponseEntity<>(auth, HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:4321")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDTO loginDTO) {
        AuthResponse auth = authServices.login(loginDTO);
        return new ResponseEntity<>(auth, HttpStatus.OK);
    }
}
