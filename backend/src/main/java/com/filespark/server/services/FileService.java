package com.filespark.server.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.filespark.server.api.aws.s3.S3;
import com.filespark.server.api.mongodb.models.File;
import com.filespark.server.api.mongodb.repository.FileRepository;
import com.filespark.server.responses.FileSummaryResponse;
import com.filespark.server.util.Mime;

@Service
public class FileService {

    private static final long UPLOAD_URL_SECONDS = 15L * 60;
    private static final long VIEW_URL_SECONDS = 60L * 60;

    private final FileRepository fileRepository;
    private final S3 s3;
    private final String region;
    private final String publicBaseUrl;

    public FileService(
            FileRepository fileRepository,
            S3 s3,
            @Value("${spring.data.aws.region}") String region,
            @Value("${spring.data.public.base.url}") String publicBaseUrl
    ) {

        this.fileRepository = fileRepository;
        this.s3 = s3;
        this.region = region;
        this.publicBaseUrl = stripTrailingSlash(publicBaseUrl);

    }

    public Presigned createPresignedUpload(String userId, String filename, String mime) {

        String extension = Mime.getExtensionFromMime(mime);
        String key = "owners/" + userId + "/" + UUID.randomUUID() + "." + extension;

        File file = new File(userId, null, filename, mime, extension, s3.getBucket(), key, 0L, region, "private", null);
        file = fileRepository.save(file);

        String uploadUrl = s3.generatePresignedPutUrl(key, mime, UPLOAD_URL_SECONDS);
        String viewUrl = publicBaseUrl + "/f/" + userId + "/" + file.getId();

        return new Presigned(file.getId(), key, mime, extension, uploadUrl, viewUrl, filename);

    }

    public List<FileSummaryResponse> listUserFiles(String userId) {

        List<File> files = fileRepository.findByOwnerIdOrderByCreatedAtDesc(userId);
        return files.stream()
                .filter(f -> !f.isDeleted())
                .map(f -> new FileSummaryResponse(
                        f.getId(),
                        f.getOriginalName(),
                        f.getMime(),
                        f.getSizeBytes(),
                        f.getCreatedAt(),
                        publicBaseUrl + "/f/" + f.getOwnerId() + "/" + f.getId()
                ))
                .collect(Collectors.toList());

    }

    public FileView getFileView(String userId, String fileId) {

        File file = fileRepository.findById(fileId).orElseThrow(() -> new IllegalArgumentException("File not found."));
        if (file.isDeleted()) throw new IllegalArgumentException("File not found.");
        if (!userId.equals(file.getOwnerId())) throw new IllegalArgumentException("File not found.");
        String signedUrl = s3.generatePresignedGetUrl(file.getKey(), VIEW_URL_SECONDS);
        return new FileView(file, signedUrl);

    }

    private static String stripTrailingSlash(String url) {

        if (url == null) return "";
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;

    }

    public record Presigned(String fileId, String key, String mime, String extension, String uploadUrl, String viewUrl, String originalFilename) {}

    public record FileView(File file, String signedUrl) {}

}
