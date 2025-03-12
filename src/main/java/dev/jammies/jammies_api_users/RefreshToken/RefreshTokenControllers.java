package dev.jammies.jammies_api_users.RefreshToken;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class RefreshTokenControllers   {
    private final RefreshTokenServices refreshTokenServices;

    public RefreshTokenControllers(RefreshTokenServices refreshTokenServices) {
        this.refreshTokenServices = refreshTokenServices;
    }
    @PostMapping("/refresh")
    public ResponseEntity<TokensResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        TokensResponse tokensResponse = refreshTokenServices.refreshToken(request.getRefreshToken());

        if (tokensResponse.getAccessToken() == null) {
            return ResponseEntity.status(401).body(tokensResponse);
        }

        return ResponseEntity.ok(tokensResponse);
    }
}
