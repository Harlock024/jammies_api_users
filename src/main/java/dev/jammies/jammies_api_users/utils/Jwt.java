package dev.jammies.jammies_api_users.utils;

import dev.jammies.jammies_api_users.RefreshToken.TokensResponse;
import dev.jammies.jammies_api_users.users.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class Jwt {

    private static final String ACCESS_SECRET =
        "a57c3a28b026c4580c22d92082e8b68b63b7521c841bc02fe971ff658e736bc3073e1c62e3fb0e184444975c986fd62b3ad696239e273fd6ac4137cf4376d7c675f29306f0d342f6eeea9309e81d72295bd502df28aaf2a2e2a5b72aed3a01e743ffacf39af284fc554bd3fc22175996dbb9f566240937ad565524c4d550be562875d37064ca775076af20902121ef384156fb084c7f39bfa7c5774870989a44291e633a47bf0cc0a9f0a7c8fd9fc4e98d3177ef16e30b2c80ce3f2fcf041727fcea1bff84408270f7c94674b66248e7299bb516151549e3f91c79c64b18fd564dd316ac8c20c9f88632ec2dbb55d1b44aac788a88f01e42fe245dbf4810cf60";
    private static final String REFRESH_SECRET =
        "39b80fb71284c083ff96daf5c9017f12a9c57292e4e2bfbda5603d9bccc465977b8b8e87200e7da47f04c7f2612b142d127fdb3dbe30520c546079d08b529e6adb0359703d04ed140a5d676bd8bbf7aef534516e666f1c1283858eaf3572d5d3d9fb6e550cbf83aae713d4ecef7875cc971e5236d67a63b7139ce64a0e8fb5620cccd2b8667a86e2fe287e8fb25750eae423252454660596a5bae27856522674d84a26b5cbd7fa8707b7ff285e6c791e4466e0ede5182ea5ff755364dc1f35e2c788d31267618ba723752c18ed8c6eac08a8d93cf5481261344deaede3f3423c0d613f5651a8498492335628606f9af37939dfda2ff5ec8c7cbf84d5cf629eb4";
    private static final long ACCESS_EXPIRATION = 86400000;
    private static final long REFRESH_EXPIRATION = 1209600000;

    public TokensResponse generateTokens(User user, String jti) {
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user, jti);
        Boolean isAuthenticated = true;
        return new TokensResponse(accessToken, refreshToken, isAuthenticated);
    }

    public static String generateAccessToken(User user) {
        return Jwts.builder()
            .setSubject(user.getId().toString())
            .setIssuedAt(new Date())
            .setExpiration(
                new Date(System.currentTimeMillis() + ACCESS_EXPIRATION)
            )
            .signWith(
                Keys.hmacShaKeyFor(ACCESS_SECRET.getBytes()),
                SignatureAlgorithm.HS512
            )
            .compact();
    }

    public static String generateRefreshToken(User user, String jti) {
        SecretKey key = Keys.hmacShaKeyFor(REFRESH_SECRET.getBytes());

        return Jwts.builder()
            .setSubject(user.getId().toString())
            .claim("jti", jti)
            .setIssuedAt(new Date())
            .setExpiration(
                new Date(System.currentTimeMillis() + REFRESH_EXPIRATION)
            )
            .signWith(
                Keys.hmacShaKeyFor(REFRESH_SECRET.getBytes()),
                SignatureAlgorithm.HS512
            )
            .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(ACCESS_SECRET.getBytes()))
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public boolean validateAccessToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(ACCESS_SECRET.getBytes()))
                .build()
                .parseClaimsJws(token);
            Date expiration = claims.getBody().getExpiration();
            if (expiration.before(new Date())) {
                throw new JwtException("token has expired");
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
