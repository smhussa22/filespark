package com.filespark.server.api.mongodb.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "stats")
public class Stats {

    public static final String SINGLETON_ID = "global";

    @Id
    private String id;

    private long totalUsers;
    private long totalUploads;

    public Stats() {

        this.id = SINGLETON_ID;
        this.totalUsers = 0;
        this.totalUploads = 0;

    }

    public String getId() { return id; }
    public long getTotalUsers() { return totalUsers; }
    public long getTotalUploads() { return totalUploads; }

    public void setId(String id) { this.id = id; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
    public void setTotalUploads(long totalUploads) { this.totalUploads = totalUploads; }

}
