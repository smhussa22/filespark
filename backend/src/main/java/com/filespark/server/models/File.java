package com.filespark.server.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "files")
public class File {

    @Id
    private String id;

    private String ownerId;
    private String clusterId;

    private String originalName;
    private String mime;
    private String extension;

    private String bucket;
    private String key;
    private String region;
    private long sizeBytes;
    private String checksum;

    private String visibility;
    private String status;

    private long viewCount;
    private long downloadCount;

    private boolean isDeleted;
    private Instant deletedAt;

    private Instant createdAt;

    public File(String ownerId, String clusterId, String originalName, String mime, String extension, String bucket, String key, long sizeBytes, String region,  String visibility, String checksum) {

        this.ownerId = ownerId;
        this.clusterId = clusterId;

        this.originalName = originalName;
        this.mime = mime;
        this.extension = extension;

        this.bucket = bucket;
        this.key = key;
        this.region = region;
        this.sizeBytes = sizeBytes;
        this.checksum = checksum;

        this.visibility = visibility != null ? visibility : "private";
        this.status = "uploading";

        this.viewCount = 0;
        this.downloadCount = 0;

        this.isDeleted = false;
        this.deletedAt = null;

        this.createdAt = Instant.now();

    }

    public String getId() {

        return id;
        
    }

    public String getOwnerId() {

        return ownerId;

    }

    public String getClusterId() {

        return clusterId;

    }

    public String getOriginalName() {

        return originalName;

    }

    public String getMime() {

        return mime;

    }

    public String getExtension() {

        return extension;

    }

    public String getBucket() {

        return bucket;

    }

    public String getKey() {

        return key;

    }

    public String getRegion() {

        return region;

    }

    public long getSizeBytes() {

        return sizeBytes;

    }

    public String getChecksum() {

        return checksum;

    }

    public String getVisibility() {

        return visibility;

    }

    public String getStatus() {

        return status;

    }

    public long getViewCount() {

        return viewCount;

    }

    public long getDownloadCount() {

        return downloadCount;

    }

    public boolean isDeleted() {

        return isDeleted;

    }

    public Instant getDeletedAt() {

        return deletedAt;

    }

    public Instant getCreatedAt() {

        return createdAt;

    }

}
