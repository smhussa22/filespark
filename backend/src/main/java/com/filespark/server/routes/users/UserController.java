package com.filespark.server.routes.users;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.filespark.server.api.mongodb.models.User;
import com.filespark.server.api.mongodb.repository.UserRepository;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {

        this.userRepository = userRepository;

    }

    @GetMapping("/users/me")
    public ResponseEntity<Map<String, Object>> me(@AuthenticationPrincipal Jwt jwt) {

        String userId = jwt.getSubject();

        return userRepository.findById(userId)
            .map(user -> {

                Map<String, Object> body = new HashMap<>();
                body.put("id", user.getId());
                body.put("email", user.getEmail());
                body.put("name", user.getName());
                body.put("picture", user.getPicture());
                return ResponseEntity.ok(body);

            })
            .orElseGet(() -> ResponseEntity.notFound().build());

    }

}
