package com.example.account_service.config.security;

import com.example.account_service.domain.util.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class UserServiceHttpSecurityConfig {

    private AuthenticationEntryPoint authenticationEntryPoint;
    private AccessDeniedHandler accessDeniedHandler;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Autowired
    public UserServiceHttpSecurityConfig(AuthenticationEntryPoint authenticationEntryPoint, AccessDeniedHandler accessDeniedHandler) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) {
        return http
                .cors(Customizer.withDefaults())
                .csrf(csrfConfig -> csrfConfig.disable())
                .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authReqConfig -> {
                    buildRequestMatchers(authReqConfig);
                })
                .exceptionHandling(exceptionConf -> {
                    exceptionConf.authenticationEntryPoint(authenticationEntryPoint);
                    exceptionConf.accessDeniedHandler(accessDeniedHandler);
                })
                .oauth2ResourceServer(oauth2ResouerceServerConfig -> {
                    oauth2ResouerceServerConfig.jwt(jwtConfigurer ->
                            jwtConfigurer
                                    .decoder(JwtDecoders.fromIssuerLocation(issuerUri))
                                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                    );
                })
                .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("role");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    private static void buildRequestMatchers(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authReqConfig) {

        authReqConfig.requestMatchers(HttpMethod.POST, "/account")
                        .hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.BANK_OPERATOR.name());

        authReqConfig.requestMatchers(HttpMethod.PATCH, "/account/balance/me")
                .hasAnyRole(RoleEnum.CUSTOMER.name());

        authReqConfig.requestMatchers(HttpMethod.PATCH, "/account/balance")
                .hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.BANK_OPERATOR.name());

        authReqConfig.requestMatchers(HttpMethod.PATCH, "/account/status")
                .hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.BANK_OPERATOR.name());

        authReqConfig.requestMatchers(HttpMethod.GET, "/account/{accountId}")
                .hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.BANK_OPERATOR.name());


        authReqConfig.anyRequest().authenticated();

    }
}
