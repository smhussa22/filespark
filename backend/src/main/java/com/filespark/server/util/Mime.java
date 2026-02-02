package com.filespark.server.util;

import java.util.Map;

import org.springframework.util.InvalidMimeTypeException;

public class Mime {

    private static final Map<String, String> mimeMap = Map.ofEntries(

        Map.entry("image/png", "png"),
        Map.entry("image/jpeg", "jpg"),  
        Map.entry("image/webp", "webp"),
        Map.entry("image/heic", "heic"),

        Map.entry("video/mp4", "mp4"),
        Map.entry("video/quicktime", "mov"), 
        Map.entry("video/x-matroska", "mkv"), 

        Map.entry("application/pdf", "pdf"),
        Map.entry("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx"), 
        Map.entry("application/zip", "zip"),
        Map.entry("application/x-rar-compressed", "rar"),
        Map.entry("application/x-7z-compressed", "7z"),

        Map.entry("text/plain", "txt")

    );

    public static String getExtensionFromMime(String mime) {

        if (mime == null || mime.isBlank()) { 

            throw new InvalidMimeTypeException(mime, "com.filespark.server.util.Mime: MIME type not found.");

        }

        String extension = mimeMap.get(mime.toLowerCase());
        if (extension == null) { 

            throw new InvalidMimeTypeException(mime, "com.filespark.server.util.Mime: MIME type not supported.");

        }

        return extension;

    }
    
}
