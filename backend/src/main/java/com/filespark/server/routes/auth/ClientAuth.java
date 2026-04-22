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
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth/google")
public class ClientAuth {

    @Value("${spring.data.google.desktop.client.id}")
    private String clientId;

    @Value("${spring.data.google.desktop.client.secret}")
    private String clientSecret;

    private final GoogleAuthService googleAuthService;
    private final AuthService authService;
    private final JWTService jwtService;

    @Autowired
    public ClientAuth(GoogleAuthService googleAuthService, AuthService authService, JWTService jwtService) {
        this.googleAuthService = googleAuthService;
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @GetMapping("/login")
    public ResponseEntity<Void> login(@RequestParam("port") int port) {
        String redirectUri = "http://localhost:" + port + "/callback";

        URI googleAuthUri = UriComponentsBuilder
            .fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
            .queryParam("client_id", clientId)
            .queryParam("redirect_uri", redirectUri)
            .queryParam("response_type", "code")
            .queryParam("scope", "openid email profile")
            .queryParam("access_type", "offline")
            .queryParam("prompt", "consent")
            .build()
            .toUri();

        return ResponseEntity.status(HttpStatus.FOUND).location(googleAuthUri).build();
    }

    @PostMapping("/session")
    public ResponseEntity<?> session(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        String redirectUri = body.get("redirect_uri");

        if (code == null || redirectUri == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing code or redirect_uri"));
        }

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

            Map<String, Object> userDto = new HashMap<>();
            userDto.put("id", user.getId());
            userDto.put("email", user.getEmail());
            userDto.put("name", user.getName());
            userDto.put("picture", user.getPicture());

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("token", sessionToken);
            responseBody.put("user", userDto);

            return ResponseEntity.ok(responseBody);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }
}
