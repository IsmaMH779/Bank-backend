package com.example.auth_service.application.service;


import com.example.auth_service.adapters.out.UserRepository;
import com.example.auth_service.adapters.out.security.CustomOAuth2UserPrincipal;
import com.example.auth_service.domain.model.User;
import com.example.auth_service.domain.util.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private UserRepository userRepository;

    @Autowired
    public CustomOAuth2UserService(UserRepository userRepository, UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
    }


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // Delegate to the service to obtain google acc information
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);


        String googleSub = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        // search or create new user
        Optional<User> optionalUser = userRepository.findByGoogleSubject(googleSub);
        User user;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();

            return new CustomOAuth2UserPrincipal(user, oAuth2User.getAttributes());
        }

        optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            user.setGoogleSubject(googleSub);
            userRepository.save(user);

            return new CustomOAuth2UserPrincipal(user, oAuth2User.getAttributes());
        }

        user = User.builder()
                .email(email)
                .googleSubject(googleSub)
                .profileImage(picture)
                .username(name + "_" + random10Digits())
                .password("{noop}OAUTH_USER_NO_PASSWORD_")
                .role(RoleEnum.CUSTOMER)
                .build();

        userRepository.save(user);

        return new CustomOAuth2UserPrincipal(user, oAuth2User.getAttributes());

    }


    private String random10Digits() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int num = (int) (Math.random() * 10);
            sb.append(num);
        }
        return sb.toString();
    }
}