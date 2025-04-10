package dev.jammies.jammies_api_users.users;

import dev.jammies.jammies_api_users.utils.Jwt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserControllers {

    private  final  UsersRepository userRepository;
    public UserControllers(UsersRepository userRepository) {
        this.userRepository = userRepository;
    }

    @CrossOrigin(origins =  "http://localhost:4321")
    @GetMapping()
    public ResponseEntity<User> getCurrentUser(Authentication authentication ) {
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
