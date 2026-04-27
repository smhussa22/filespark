package com.filespark.server.routes.files;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.filespark.server.responses.FileMetaResponse;
import com.filespark.server.services.FileService;
import com.filespark.server.services.FileService.FileView;
import com.filespark.server.services.FileService.ForbiddenException;

@RestController
@RequestMapping("/f")
public class PublicFileController {

    private final FileService fileService;

    public PublicFileController(FileService fileService) {

        this.fileService = fileService;

    }

    @GetMapping("/{userId}/{fileId}/meta")
    public ResponseEntity<FileMetaResponse> meta(@PathVariable("userId") String userId, @PathVariable("fileId") String fileId) {

        try {

            String requesterId = currentRequesterId();
            FileView view = fileService.getFileView(requesterId, userId, fileId, true);
            return ResponseEntity.ok(new FileMetaResponse(
                    view.file().getId(),
                    view.file().getOwnerId(),
                    view.file().getOriginalName(),
                    view.file().getMime(),
                    view.file().getSizeBytes(),
                    view.signedUrl(),
                    view.file().getVisibility(),
                    view.file().getViewCount(),
                    view.file().getDownloadCount(),
                    view.isOwner()
            ));

        }
        catch (ForbiddenException exception) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
        catch (IllegalArgumentException exception) {

            return ResponseEntity.notFound().build();

        }

    }

    @GetMapping("/{userId}/{fileId}/raw")
    public ResponseEntity<Void> raw(@PathVariable("userId") String userId, @PathVariable("fileId") String fileId) {

        try {

            String requesterId = currentRequesterId();
            FileView view = fileService.getFileView(requesterId, userId, fileId, false);
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(view.signedUrl())).build();

        }
        catch (ForbiddenException exception) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
        catch (IllegalArgumentException exception) {

            return ResponseEntity.notFound().build();

        }

    }

    @GetMapping("/{userId}/{fileId}/download")
    public ResponseEntity<Void> download(@PathVariable("userId") String userId, @PathVariable("fileId") String fileId) {

        try {

            String requesterId = currentRequesterId();
            FileView view = fileService.getFileDownload(requesterId, userId, fileId);

            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(view.signedUrl())).build();

        }
        catch (ForbiddenException exception) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
        catch (IllegalArgumentException exception) {

            return ResponseEntity.notFound().build();

        }

    }

    private static String currentRequesterId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) return null;
        Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt jwt) return jwt.getSubject();
        return null;

    }

}
