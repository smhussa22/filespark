package com.filespark.server.services;

import java.util.List;
import java.util.Set;
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

    public static final String VISIBILITY_PRIVATE = "private";
    public static final String VISIBILITY_UNLISTED = "unlisted";
    public static final String VISIBILITY_PUBLIC = "public";

    private static final Set<String> ALLOWED_VISIBILITIES = Set.of(VISIBILITY_PRIVATE, VISIBILITY_UNLISTED, VISIBILITY_PUBLIC);

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

        File file = new File(userId, null, filename, mime, extension, s3.getBucket(), key, 0L, region, VISIBILITY_PRIVATE, null);
        file = fileRepository.save(file);

        String uploadUrl = s3.generatePresignedPutUrl(key, mime, UPLOAD_URL_SECONDS);
        String viewUrl = publicBaseUrl + "/f/" + userId + "/" + file.getId();

        return new Presigned(file.getId(), key, mime, extension, uploadUrl, viewUrl, filename);

    }

    public List<FileSummaryResponse> listUserFiles(String userId) {

        List<File> files = fileRepository.findByOwnerIdOrderByCreatedAtDesc(userId);
        return files.stream()
                .filter(f -> !f.isDeleted())
                .filter(f -> !VISIBILITY_UNLISTED.equalsIgnoreCase(f.getVisibility()))
                .map(f -> new FileSummaryResponse(
                        f.getId(),
                        f.getOriginalName(),
                        f.getMime(),
                        f.getSizeBytes(),
                        f.getCreatedAt(),
                        publicBaseUrl + "/f/" + f.getOwnerId() + "/" + f.getId(),
                        normalizeVisibility(f.getVisibility()),
                        f.getViewCount(),
                        f.getDownloadCount()
                ))
                .collect(Collectors.toList());

    }

    public void deleteFile(String userId, String fileId) {

        File file = fileRepository.findById(fileId).orElseThrow(() -> new IllegalArgumentException("File not found."));
        if (!userId.equals(file.getOwnerId())) throw new IllegalArgumentException("File not found.");

        s3.deleteObject(file.getKey());
        fileRepository.delete(file);

    }

    public FileView getFileView(String requesterId, String ownerId, String fileId, boolean countView) {

        File file = fileRepository.findById(fileId).orElseThrow(() -> new IllegalArgumentException("File not found."));
        if (file.isDeleted()) throw new IllegalArgumentException("File not found.");
        if (!ownerId.equals(file.getOwnerId())) throw new IllegalArgumentException("File not found.");

        String visibility = normalizeVisibility(file.getVisibility());
        boolean isOwner = requesterId != null && requesterId.equals(file.getOwnerId());

        if (VISIBILITY_PRIVATE.equals(visibility) && !isOwner) throw new ForbiddenException("File is private.");

        if (countView && !isOwner) {

            file.incrementViewCount();
            fileRepository.save(file);

        }

        String signedUrl = s3.generatePresignedGetUrl(file.getKey(), VIEW_URL_SECONDS);
        return new FileView(file, signedUrl, isOwner);

    }

    public FileView getFileDownload(String requesterId, String ownerId, String fileId) {

        File file = fileRepository.findById(fileId).orElseThrow(() -> new IllegalArgumentException("File not found."));
        if (file.isDeleted()) throw new IllegalArgumentException("File not found.");
        if (!ownerId.equals(file.getOwnerId())) throw new IllegalArgumentException("File not found.");

        String visibility = normalizeVisibility(file.getVisibility());
        boolean isOwner = requesterId != null && requesterId.equals(file.getOwnerId());

        if (VISIBILITY_PRIVATE.equals(visibility) && !isOwner) throw new ForbiddenException("File is private.");

        if (!isOwner) {

            file.incrementDownloadCount();
            fileRepository.save(file);

        }

        String signedUrl = s3.generatePresignedGetUrl(file.getKey(), VIEW_URL_SECONDS);
        return new FileView(file, signedUrl, isOwner);

    }

    public void setVisibility(String userId, String fileId, String visibility) {

        String normalized = normalizeVisibility(visibility);
        if (!ALLOWED_VISIBILITIES.contains(normalized)) throw new IllegalArgumentException("Invalid visibility.");

        File file = fileRepository.findById(fileId).orElseThrow(() -> new IllegalArgumentException("File not found."));
        if (file.isDeleted()) throw new IllegalArgumentException("File not found.");
        if (!userId.equals(file.getOwnerId())) throw new IllegalArgumentException("File not found.");

        file.setVisibility(normalized);
        fileRepository.save(file);

    }

    private static String normalizeVisibility(String visibility) {

        if (visibility == null) return VISIBILITY_PRIVATE;
        String lower = visibility.toLowerCase();
        if (ALLOWED_VISIBILITIES.contains(lower)) return lower;
        return VISIBILITY_PRIVATE;

    }

    private static String stripTrailingSlash(String url) {

        if (url == null) return "";
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;

    }

    public record Presigned(String fileId, String key, String mime, String extension, String uploadUrl, String viewUrl, String originalFilename) {}

    public record FileView(File file, String signedUrl, boolean isOwner) {}

    public static class ForbiddenException extends RuntimeException {

        public ForbiddenException(String message) {

            super(message);

        }

    }

}
