package com.filespark.server.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileMetaResponse {

    private final String id;
    private final String ownerId;
    private final String name;
    private final String mime;
    private final long sizeBytes;
    private final String signedUrl;
    private final String visibility;
    private final long viewCount;
    private final long downloadCount;
    private final boolean isOwner;

    public FileMetaResponse(String id, String ownerId, String name, String mime, long sizeBytes, String signedUrl, String visibility, long viewCount, long downloadCount, boolean isOwner) {

        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.mime = mime;
        this.sizeBytes = sizeBytes;
        this.signedUrl = signedUrl;
        this.visibility = visibility;
        this.viewCount = viewCount;
        this.downloadCount = downloadCount;
        this.isOwner = isOwner;

    }

    public String getId() { return id; }
    public String getOwnerId() { return ownerId; }
    public String getName() { return name; }
    public String getMime() { return mime; }
    public long getSizeBytes() { return sizeBytes; }
    public String getSignedUrl() { return signedUrl; }
    public String getVisibility() { return visibility; }
    public long getViewCount() { return viewCount; }
    public long getDownloadCount() { return downloadCount; }
    @JsonProperty("isOwner")
    public boolean getIsOwner() { return isOwner; }

}
