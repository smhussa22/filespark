package com.filespark.server.routes.users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.filespark.server.api.mongodb.models.User;
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
                body.put("browseDirectories", user.getBrowseDirectories());
                body.put("downloadsHidden", user.isDownloadsHidden());
                return ResponseEntity.ok(body);

            })
            .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PutMapping("/users/me/directories")
    public ResponseEntity<?> setBrowseDirectories(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody Map<String, Object> body
    ) {

        String userId = jwt.getSubject();

        Object raw = body.get("directories");
        List<String> directories = new ArrayList<>();
        if (raw instanceof List<?> list) {

            for (Object item : list) if (item instanceof String s && !s.isBlank()) directories.add(s);

        }

        return userRepository.findById(userId)
            .<ResponseEntity<?>>map(user -> {

                user.setBrowseDirectories(directories);
                User saved = userRepository.save(user);
                return ResponseEntity.ok(Map.of("browseDirectories", saved.getBrowseDirectories()));

            })
            .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PatchMapping("/users/me/downloads-hidden")
    public ResponseEntity<?> setDownloadsHidden(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody Map<String, Object> body
    ) {

        String userId = jwt.getSubject();

        Object raw = body.get("hidden");
        boolean hidden = raw instanceof Boolean b ? b : false;

        return userRepository.findById(userId)
            .<ResponseEntity<?>>map(user -> {

                user.setDownloadsHidden(hidden);
                User saved = userRepository.save(user);
                return ResponseEntity.ok(Map.of("downloadsHidden", saved.isDownloadsHidden()));

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
