package com.example.auth_service.adapters.out.security;

import com.example.auth_service.domain.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2UserPrincipal extends User implements OAuth2User {

    private Map<String, Object> attributes;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomOAuth2UserPrincipal(User user, Map<String, Object> attributes) {
        this.setUsername(user.getUsername());
        this.setId(user.getId());
        this.setEmail(user.getEmail());
        this.setGoogleSubject(user.getGoogleSubject());
        this.setRole(user.getRole());
        this.setProfileImage(user.getProfileImage());
        this.setPassword(user.getPassword());

        this.authorities = user.getAuthorities();
        this.attributes = attributes;
    }


    @Override
    public String getName() {
        return this.getEmail();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}