package com.filespark.server.responses;

public class FileMetaResponse {

    private final String id;
    private final String name;
    private final String mime;
    private final long sizeBytes;
    private final String signedUrl;

    public FileMetaResponse(String id, String name, String mime, long sizeBytes, String signedUrl) {

        this.id = id;
        this.name = name;
        this.mime = mime;
        this.sizeBytes = sizeBytes;
        this.signedUrl = signedUrl;

    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getMime() { return mime; }
    public long getSizeBytes() { return sizeBytes; }
    public String getSignedUrl() { return signedUrl; }

}
