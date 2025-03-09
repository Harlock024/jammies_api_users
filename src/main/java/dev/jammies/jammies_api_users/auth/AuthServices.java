package dev.jammies.jammies_api_users.auth;

import dev.jammies.jammies_api_users.RefreshToken.RefreshTokenServices;
import dev.jammies.jammies_api_users.RefreshToken.TokensResponse;
import dev.jammies.jammies_api_users.users.UsersRepository;
import dev.jammies.jammies_api_users.users.User;
import dev.jammies.jammies_api_users.users.UserDTO;
import dev.jammies.jammies_api_users.utils.Jwt;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServices {

    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    private final RefreshTokenServices refreshTokenServices;
    private final Jwt jwt;

    public AuthServices(UsersRepository usersRepository, PasswordEncoder passwordEncoder , RefreshTokenServices refreshTokenServices, Jwt jwt) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenServices = refreshTokenServices;
        this.jwt = jwt;
    }

    public User signup(@NotNull UserDTO newUser) {
        User user = new User();
        user.setEmail(newUser.getEmail());
        user.setUsername(newUser.getUsername());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        var userExisted = usersRepository.findByEmail(user.getEmail());
        return userExisted.orElseGet(() -> usersRepository.save(user));
    }
    public TokensResponse login(@NotNull LoginDTO loginUser) {
        User userlogin = usersRepository.findByEmail(loginUser.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        try {
            if (!passwordEncoder.matches(loginUser.getPassword(), userlogin.getPassword())) {
                throw new BadCredentialsException("Wrong password");
            }

            String jti = UUID.randomUUID().toString();
            TokensResponse tokensResponse= jwt.generateTokens(userlogin,jti);
            refreshTokenServices.createRefreshToken(userlogin, tokensResponse.getRefreshToken(),jti);
            return tokensResponse;
        }catch (BadCredentialsException e) {
            throw  new BadCredentialsException("Invalid username or password");
        }
    }
}

