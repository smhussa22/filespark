package com.filespark;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.File;

public class RawFile {

    private final File file;
    private final Path path;
    private final String name;
    private final double sizeMB;
    private final String mimeType;

    public RawFile(File file) throws Exception {

        this.file = file;
        this.path = file.toPath();
        this.name = path.getFileName().toString();
        this.sizeMB = (double) Files.size(path) / 1024 / 1024;
        this.mimeType = Files.probeContentType(path);

    }

    public void printDebugInfo(){

        System.out.println("File Name: " + name);
        System.out.println("Full Path: " + path.toAbsolutePath());
        System.out.println("File Size: " + String.format("%.2f MB", sizeMB));
        System.out.println("MIME Type: " + (mimeType != null ? mimeType : "Unknown"));

    }

    public File getFile() { 
        
        return this.file;
    
    }

    public double getSizeInMB() { 

        return this.sizeMB;

    }

    public String getFileName() {

        return this.name;

    }

    public Path getPath() {

        return this.path;

    }

    public String getMediaType() {

        return this.mimeType;

    }

}
