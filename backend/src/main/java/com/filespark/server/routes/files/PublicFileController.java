package com.filespark.server.routes.files;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.filespark.server.responses.FileMetaResponse;
import com.filespark.server.services.FileService;
import com.filespark.server.services.FileService.FileView;

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

            FileView view = fileService.getFileView(userId, fileId);
            return ResponseEntity.ok(new FileMetaResponse(
                    view.file().getId(),
                    view.file().getOriginalName(),
                    view.file().getMime(),
                    view.file().getSizeBytes(),
                    view.signedUrl()
            ));

        }
        catch (IllegalArgumentException exception) {

            return ResponseEntity.notFound().build();

        }

    }

    @GetMapping("/{userId}/{fileId}/raw")
    public ResponseEntity<Void> raw(@PathVariable("userId") String userId, @PathVariable("fileId") String fileId) {

        try {

            FileView view = fileService.getFileView(userId, fileId);
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(view.signedUrl())).build();

        }
        catch (IllegalArgumentException exception) {

            return ResponseEntity.notFound().build();

        }

    }

}
