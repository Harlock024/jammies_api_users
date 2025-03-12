package dev.jammies.jammies_api_users.auth;


import dev.jammies.jammies_api_users.RefreshToken.RefreshTokenRepository;
import dev.jammies.jammies_api_users.RefreshToken.RefreshTokenServices;
import dev.jammies.jammies_api_users.RefreshToken.TokensResponse;
import dev.jammies.jammies_api_users.users.User;
import dev.jammies.jammies_api_users.users.UserDTO;
import dev.jammies.jammies_api_users.utils.Jwt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthControllers {

    private final  AuthServices authServices;


    public AuthControllers(AuthServices authServices) {
        this.authServices = authServices;
    }

    @PostMapping("/register")
    public ResponseEntity<User> sign_up(@RequestBody UserDTO newUser) {
        User userExisted= authServices.signup(newUser);
        return new ResponseEntity<>(userExisted, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokensResponse> login(@RequestBody LoginDTO loginDTO) {
        TokensResponse user =  authServices.login(loginDTO);
        return new ResponseEntity<>(user,HttpStatus.OK);

    }
}

