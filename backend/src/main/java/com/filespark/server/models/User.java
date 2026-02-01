package com.filespark.server.models;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String email;
    private String name;
    private String googleId;
    private String picture;
    private Instant createdAt;
    private Instant updatedAt;

    public User(String email, String name, String googleId, String picture, Instant createdAt, Instant updatedAt) {

        this.email = email;
        this.name = name;
        this.googleId = googleId;
        this.picture = picture;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();

    }

    public String getId() {

        return id;

    }

    public String getGoogleId() {

        return googleId;

    }

    public String getEmail() {

        return email;

    }

    public String getName() {

        return name;

    }

    public String getPicture() {

        return picture;

    }

    public Instant getCreatedAt() {

        return createdAt;

    }

    public Instant getUpdatedAt() {

        return updatedAt;

    }

    public void setUpdatedAt(Instant updatedAt) {

        this.updatedAt = updatedAt;

    }
    
}
