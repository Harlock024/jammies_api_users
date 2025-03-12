package dev.jammies.jammies_api_users.utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
@RequiredArgsConstructor
@WebFilter("/api/*")
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final Jwt jwtService;
    private final TokenHasher tokenHasher;
    private final UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
         String refreshToken = request.getHeader("Authorization");

        if(refreshToken == null || !refreshToken.startsWith("Bearer ")) {
            refreshToken =refreshToken.substring(7);

            String jwtToken = request.getHeader("Authorization");
            if(jwtToken == null || !jwtService.validateAccessToken(jwtToken)) {

            }
    }
        
        
    }
}
