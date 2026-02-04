package com.example.auth_service.config.security;

import com.example.auth_service.domain.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomOAuth2TokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    @Override
    public void customize(JwtEncodingContext context) {
        Authentication authentication = context.getPrincipal();

        String tokenType = context.getTokenType().getValue();

        if (tokenType.equals("access_token")) {
            if (authentication.getPrincipal() instanceof User) {
                User user = (User) authentication.getPrincipal();

                // extract role
                List<String> authorities = authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList();

                // add new claims
                context.getClaims()
                        .claim("user_id", user.getId())
                        .claim("role", authorities);

            }
        }

    }

}