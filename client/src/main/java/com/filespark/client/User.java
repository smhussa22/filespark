package com.filespark.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private String id;
    private String email;
    private String name;
    private String picture;

    public String getId() { return this.id; }
    public String getEmail() { return this.email; }
    public String getName() { return this.name; }
    public String getPicture() { return this.picture; }

    public void setId(String id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setName(String name) { this.name = name; }
    public void setPicture(String picture) { this.picture = picture; }

}
