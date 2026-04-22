package com.filespark.server.responses;

import java.time.Instant;

public class FileSummaryResponse {

    private final String id;
    private final String name;
    private final String mime;
    private final long sizeBytes;
    private final Instant createdAt;
    private final String viewUrl;

    public FileSummaryResponse(String id, String name, String mime, long sizeBytes, Instant createdAt, String viewUrl) {

        this.id = id;
        this.name = name;
        this.mime = mime;
        this.sizeBytes = sizeBytes;
        this.createdAt = createdAt;
        this.viewUrl = viewUrl;

    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getMime() { return mime; }
    public long getSizeBytes() { return sizeBytes; }
    public Instant getCreatedAt() { return createdAt; }
    public String getViewUrl() { return viewUrl; }

}
