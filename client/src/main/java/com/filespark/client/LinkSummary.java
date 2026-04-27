package com.filespark.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkSummary {

    public String id;
    public String ownerId;
    public String name;
    public String mime;
    public long sizeBytes;
    public String createdAt;
    public String viewUrl;
    public String visibility;
    public long viewCount;
    public long downloadCount;

}
