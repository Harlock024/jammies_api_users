package dev.jammies.jammies_api_users.auth;

import dev.jammies.jammies_api_users.RefreshToken.TokensResponse;
import dev.jammies.jammies_api_users.users.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    public TokensResponse tokensResponse;
    public User user;




}
