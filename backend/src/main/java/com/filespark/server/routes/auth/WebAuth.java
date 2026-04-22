package com.filespark.server.routes.auth;

import com.filespark.server.api.mongodb.models.User;
import com.filespark.server.services.AuthService;
import com.filespark.server.services.GoogleAuthService;
import com.filespark.server.services.JWTService;
import com.nimbusds.jwt.JWTClaimsSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/auth/google/web")
public class WebAuth {

    @Value("${spring.data.google.website.client.id}")
    private String clientId;

    @Value("${spring.data.google.website.client.secret}")
    private String clientSecret;

    @Value("${spring.data.google.website.redirect.uri}")
    private String redirectUri;

    private final GoogleAuthService googleAuthService;
    private final AuthService authService;
    private final JWTService jwtService;

    @Autowired
    public WebAuth(GoogleAuthService googleAuthService, AuthService authService, JWTService jwtService) {
        this.googleAuthService = googleAuthService;
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/callback")
    public ResponseEntity<?> callback(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        if (code == null) return ResponseEntity.badRequest().body(Map.of("error", "Missing code"));

        try {
            RestTemplate restTemplate = new RestTemplate();

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("code", code);
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("redirect_uri", redirectUri);
            params.add("grant_type", "authorization_code");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(
                "https://oauth2.googleapis.com/token",
                new HttpEntity<>(params, headers),
                Map.class
            );

            String idToken = (String) tokenResponse.getBody().get("id_token");
            JWTClaimsSet claims = googleAuthService.verifyGoogleIdToken(idToken, clientId);

            String googleId = claims.getSubject();
            String email = (String) claims.getClaim("email");
            String name = (String) claims.getClaim("name");
            String picture = (String) claims.getClaim("picture");

            User user = authService.getOrCreateUser(email, name, googleId, picture);
            String sessionToken = jwtService.issueSessionToken(user.getId());

            return ResponseEntity.ok(Map.of("token", sessionToken));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }
}
