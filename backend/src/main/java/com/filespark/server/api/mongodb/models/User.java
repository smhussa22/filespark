package com.filespark.server.api.mongodb.models;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String email;
    private String name;

    @Indexed(unique = true)
    private String googleId;
    
    private String picture;
    private Instant createdAt;
    private Instant updatedAt;

    public User(String email, String name, String googleId, String picture) {

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

    public void setGoogleId(String googleId) {

        this.googleId = googleId;

    }

    public void setName(String name) {

        this.name = name;

    }

    public void setPicture(String picture) {

        this.picture = picture;

    }
    
}
