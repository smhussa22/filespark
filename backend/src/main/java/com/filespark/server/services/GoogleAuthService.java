package com.filespark.server.services;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.JWSAlgorithm;

import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;

@Service
public class GoogleAuthService {

    private static final String jwksUrl = "https://www.googleapis.com/oauth2/v3/certs";

    private static final List<String> issuers = List.of("https://accounts.google.com", "accounts.google.com");

    public JWTClaimsSet verifyGoogleIdToken(String idToken, String clientId) throws Exception {

        // Configure JWT processor
        DefaultJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();

        // Configure Google JWKS key source
        JWKSource<SecurityContext> keySource =
                new RemoteJWKSet<>(new URL(jwksUrl));

        JWSKeySelector<SecurityContext> keySelector =
                new JWSVerificationKeySelector<>(
                        JWSAlgorithm.RS256,
                        keySource
                );

        jwtProcessor.setJWSKeySelector(keySelector);

        // Process & validate signature
        JWTClaimsSet claims = jwtProcessor.process(idToken, null);

        // Validate audience
        if (!claims.getAudience().contains(clientId)) {
            throw new RuntimeException("Invalid Google token audience");
        }

        // Validate issuer
        if (!issuers.contains(claims.getIssuer())) {
            throw new RuntimeException("Invalid Google token issuer");
        }

        return claims;
    }
}