package com.filespark.server.routes.users;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.filespark.server.api.mongodb.repository.UserRepository;
import com.filespark.server.services.FileService;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final FileService fileService;

    public UserController(UserRepository userRepository, FileService fileService) {

        this.userRepository = userRepository;
        this.fileService = fileService;

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

    @DeleteMapping("/users/me")
    public ResponseEntity<Void> deleteMe(@AuthenticationPrincipal Jwt jwt) {

        String userId = jwt.getSubject();

        fileService.deleteAllForUser(userId);
        userRepository.deleteById(userId);

        return ResponseEntity.noContent().build();

    }

}
