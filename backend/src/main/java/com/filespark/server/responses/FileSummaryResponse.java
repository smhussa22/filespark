package com.filespark.server.responses;

import java.time.Instant;

public class FileSummaryResponse {

    private final String id;
    private final String ownerId;
    private final String name;
    private final String mime;
    private final long sizeBytes;
    private final Instant createdAt;
    private final String viewUrl;
    private final String visibility;
    private final long viewCount;
    private final long downloadCount;

    public FileSummaryResponse(String id, String ownerId, String name, String mime, long sizeBytes, Instant createdAt, String viewUrl, String visibility, long viewCount, long downloadCount) {

        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.mime = mime;
        this.sizeBytes = sizeBytes;
        this.createdAt = createdAt;
        this.viewUrl = viewUrl;
        this.visibility = visibility;
        this.viewCount = viewCount;
        this.downloadCount = downloadCount;

    }

    public String getId() { return id; }
    public String getOwnerId() { return ownerId; }
    public String getName() { return name; }
    public String getMime() { return mime; }
    public long getSizeBytes() { return sizeBytes; }
    public Instant getCreatedAt() { return createdAt; }
    public String getViewUrl() { return viewUrl; }
    public String getVisibility() { return visibility; }
    public long getViewCount() { return viewCount; }
    public long getDownloadCount() { return downloadCount; }

}
