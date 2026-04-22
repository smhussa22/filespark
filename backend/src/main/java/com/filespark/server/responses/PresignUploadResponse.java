package com.filespark.server.responses;

public class PresignUploadResponse {

    private final String fileId;
    private final String key;
    private final String mime;
    private final String extension;
    private final String uploadUrl;
    private final String viewUrl;
    private final String originalFilename;

    public PresignUploadResponse(String fileId, String key, String mime, String extension, String uploadUrl, String viewUrl, String originalFilename) {

        this.fileId = fileId;
        this.key = key;
        this.mime = mime;
        this.extension = extension;
        this.uploadUrl = uploadUrl;
        this.viewUrl = viewUrl;
        this.originalFilename = originalFilename;

    }

    public String getFileId() { return fileId; }
    public String getKey() { return key; }
    public String getMime() { return mime; }
    public String getExtension() { return extension; }
    public String getUploadUrl() { return uploadUrl; }
    public String getViewUrl() { return viewUrl; }
    public String getOriginalFilename() { return originalFilename; }

}
