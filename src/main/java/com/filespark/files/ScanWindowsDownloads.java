package com.filespark.files;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.filespark.Config;

public class ScanWindowsDownloads {

    private ScanWindowsDownloads() {}

    public static List<File> getDownloadsFiles(int numberOfFilesToFetch) {

        String downloadsFilesPath = System.getProperty("user.home") + File.separator + "Downloads";
        File downloadsFolderDirectory = new File(downloadsFilesPath);
        List<File> downloadsFiles = new ArrayList<>();

        if (!downloadsFolderDirectory.exists() || !downloadsFolderDirectory.isDirectory()) {
            System.err.println("[ScanWindowsDownloads]: DNE/!directory " + downloadsFilesPath);
            return downloadsFiles;
        }

        File[] files = downloadsFolderDirectory.listFiles(File::isFile);

        if (files == null) {
            System.err.println("[ScanWindowsDownloads]: Files NULL");
            return downloadsFiles;
        }

        // newest → oldest
        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

        // collect up to numberOfFilesToFetch that match allowedExtensions
        for (File downloadFile : files) {

            if (downloadsFiles.size() >= numberOfFilesToFetch) {
                break;
            }

            String targetFileName = downloadFile.getName().toLowerCase();
            boolean isAllowed = Config.allowedExtensions
                    .stream()
                    .anyMatch(targetFileName::endsWith);

            if (isAllowed) {

                downloadsFiles.add(downloadFile);
            }
            else {

                System.out.println("Skipping: " + downloadFile.getName());

            }

        }

        System.out.println("[ScanWindowsDownloads]: Returning " + downloadsFiles.size() + " real files");
        return downloadsFiles;
    }
}
