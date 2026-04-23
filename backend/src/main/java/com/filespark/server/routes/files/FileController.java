package com.filespark.server.routes.files;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.filespark.server.responses.FileSummaryResponse;
import com.filespark.server.responses.PresignUploadResponse;
import com.filespark.server.services.FileService;
import com.filespark.server.services.FileService.Presigned;

@RestController
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {

        this.fileService = fileService;

    }

    @GetMapping("/presign-upload")
    public PresignUploadResponse presignUpload(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam("filename") String filename,
            @RequestParam("mime") String mime
    ) {

        String userId = jwt.getSubject();
        Presigned p = fileService.createPresignedUpload(userId, filename, mime);

        return new PresignUploadResponse(p.fileId(), p.key(), p.mime(), p.extension(), p.uploadUrl(), p.viewUrl(), p.originalFilename());

    }

    @GetMapping("/files")
    public List<FileSummaryResponse> listFiles(@AuthenticationPrincipal Jwt jwt) {

        String userId = jwt.getSubject();
        return fileService.listUserFiles(userId);

    }

    @DeleteMapping("/files/{fileId}")
    public void deleteFile(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("fileId") String fileId
    ) {

        String userId = jwt.getSubject();
        fileService.deleteFile(userId, fileId);

    }

    @PatchMapping("/files/{fileId}/visibility")
    public ResponseEntity<?> setVisibility(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("fileId") String fileId,
            @RequestBody Map<String, String> body
    ) {

        String userId = jwt.getSubject();
        String visibility = body.get("visibility");

        try {

            fileService.setVisibility(userId, fileId, visibility);
            return ResponseEntity.ok(Map.of("visibility", visibility));

        }
        catch (IllegalArgumentException exception) {

            return ResponseEntity.badRequest().body(Map.of("error", exception.getMessage()));

        }

    }

}
