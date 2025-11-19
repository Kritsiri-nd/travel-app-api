package com.travelapp.travel_api.security.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        Map<String, Object> attributes = oAuth2User.getAttributes();
        if ("line".equalsIgnoreCase(registrationId)) {
            Object email = attributes.get("email");
            if (email == null) {
                Object profile = attributes.get("profile");
                if (profile instanceof Map<?, ?> profileMap) {
                    Object emailFromProfile = profileMap.get("email");
                    if (emailFromProfile != null) {
                        attributes.put("email", emailFromProfile);
                    }
                }
            }
        }
        return oAuth2User;
    }
}

