package com.travelapp.travel_api.security.oauth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.oidc.authentication.OidcIdTokenDecoderFactory;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoderFactory;

@Configuration
public class LineOidcConfig {

    @Bean
    public JwtDecoderFactory<ClientRegistration> oidcIdTokenDecoderFactory() {
        OidcIdTokenDecoderFactory factory = new OidcIdTokenDecoderFactory();
        factory.setJwsAlgorithmResolver(clientRegistration -> {
            if ("line".equals(clientRegistration.getRegistrationId())) {
                return MacAlgorithm.HS256;
            }
            return SignatureAlgorithm.RS256;
        });
        return factory;
    }
}
