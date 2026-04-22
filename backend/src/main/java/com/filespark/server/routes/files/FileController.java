package com.filespark.server.routes.files;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
