package com.filespark.client;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private String id;
    private String email;
    private String name;
    private String picture;
    private List<String> browseDirectories = new ArrayList<>();

    public String getId() { return this.id; }
    public String getEmail() { return this.email; }
    public String getName() { return this.name; }
    public String getPicture() { return this.picture; }
    public List<String> getBrowseDirectories() { return this.browseDirectories == null ? new ArrayList<>() : this.browseDirectories; }

    public void setId(String id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setName(String name) { this.name = name; }
    public void setPicture(String picture) { this.picture = picture; }
    public void setBrowseDirectories(List<String> browseDirectories) { this.browseDirectories = browseDirectories == null ? new ArrayList<>() : browseDirectories; }

}
