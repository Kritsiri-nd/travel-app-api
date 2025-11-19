package com.travelapp.travel_api.security.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelapp.travel_api.entity.User;
import com.travelapp.travel_api.repository.UserRepository;
import com.travelapp.travel_api.security.jwt.JwtTokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PasswordEncoder passwordEncoder;

    public OAuth2AuthenticationSuccessHandler(JwtTokenProvider jwtTokenProvider,
                                              UserRepository userRepository,
                                              PasswordEncoder passwordEncoder) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = extractEmail(oAuth2User);
        if (!StringUtils.hasText(email)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email scope is required");
            return;
        }

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(createUserFromOauth(oAuth2User, email)));

        Authentication auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                user.getEmail(), null, java.util.List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER"))
        );
        String token = jwtTokenProvider.generateToken(auth);

        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        Map<String, Object> body = new HashMap<>();
        body.put("token", token);
        body.put("email", user.getEmail());
        body.put("displayName", user.getDisplayName());

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }

    private User createUserFromOauth(OAuth2User oAuth2User, String email) {
        User user = new User();
        user.setEmail(email);
        user.setDisplayName((String) oAuth2User.getAttributes().getOrDefault("name", email));
        user.setPasswordHash(passwordEncoder.encode(UUID.randomUUID().toString()));
        return user;
    }

    private String extractEmail(OAuth2User oAuth2User) {
        Object email = oAuth2User.getAttributes().get("email");
        if (email == null) {
            Object emails = oAuth2User.getAttributes().get("emails");
            if (emails instanceof java.util.List<?> list && !list.isEmpty()) {
                Object first = list.get(0);
                if (first instanceof Map<?, ?> map) {
                    Object value = map.get("value");
                    if (value != null) {
                        return value.toString();
                    }
                }
            }
            return null;
        }
        return email.toString();
    }
}

