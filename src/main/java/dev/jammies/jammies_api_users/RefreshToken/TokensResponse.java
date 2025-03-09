package dev.jammies.jammies_api_users.RefreshToken;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Setter
@Getter
public class TokensResponse {
    private String accessToken;
    private String refreshToken;
    private Boolean isAuthenticated;

    public TokensResponse(String accessToken, String refreshToken, Boolean isAuthenticated) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.isAuthenticated = isAuthenticated;
    }
}
