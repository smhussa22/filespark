package com.filespark.server.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "file_clusters")
public class FileCluster {

    @Id
    private String id;

    private String ownerId;
    private String name;
    private Instant createdAt;
    private boolean isDeleted;

    public FileCluster(String ownerId, String name) {

        this.ownerId = ownerId;
        this.name = name;
        this.createdAt = Instant.now();
        this.isDeleted = false;

    }


    public String getId() {

        return id;

    }

    public String getOwnerId() {

        return ownerId;

    }

    public String getName() {

        return name;

    }

    public Instant getCreatedAt() {

        return createdAt;

    }

    public boolean isDeleted() {

        return isDeleted;

    }

}
