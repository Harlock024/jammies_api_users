package dev.jammies.jammies_api_users.auth;

import dev.jammies.jammies_api_users.RefreshToken.RefreshTokenServices;
import dev.jammies.jammies_api_users.RefreshToken.TokensResponse;
import dev.jammies.jammies_api_users.users.UserServices;
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
    private final UserServices userServices;


    public AuthServices(UsersRepository usersRepository, PasswordEncoder passwordEncoder , RefreshTokenServices refreshTokenServices, Jwt jwt, UserServices userServices) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenServices = refreshTokenServices;
        this.jwt = jwt;
        this.userServices = userServices;
    }

    public AuthResponse signup(@NotNull UserDTO newUser) {
        User user = new User();
        user.setEmail(newUser.getEmail());
        user.setUsername(newUser.getUsername());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        var userExisted = usersRepository.findByEmail(user.getEmail());
        var userRegistered= userExisted.orElseGet(() -> usersRepository.save(user));
        String jti = UUID.randomUUID().toString();
        TokensResponse tokensResponse= jwt.generateTokens(userRegistered,jti);
        refreshTokenServices.createRefreshToken(userRegistered, tokensResponse.getRefreshToken(),jti);
        AuthResponse authResponse= new AuthResponse();
        authResponse.setTokensResponse(tokensResponse);
        authResponse.setUser(userServices.userResponseDto(userRegistered));
        return authResponse;
    }
    public AuthResponse login(@NotNull LoginDTO loginUser) {
        User userLogin = usersRepository.findByEmail(loginUser.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        try {
            if (!passwordEncoder.matches(loginUser.getPassword(), userLogin.getPassword())) {
                throw new BadCredentialsException("Wrong password");
            }
            String jti = UUID.randomUUID().toString();
            TokensResponse tokensResponse= jwt.generateTokens(userLogin,jti);
            refreshTokenServices.createRefreshToken(userLogin, tokensResponse.getRefreshToken(),jti);
            AuthResponse authResponse = new AuthResponse();
            authResponse.setTokensResponse(tokensResponse);
            authResponse.setUser(userServices.userResponseDto(userLogin));
            return authResponse;
        }catch (BadCredentialsException e) {
            throw  new BadCredentialsException("Invalid username or password");
        }
    }
}
