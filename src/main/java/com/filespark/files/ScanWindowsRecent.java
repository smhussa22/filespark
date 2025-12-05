// scans users recently modified files
// @todo: literally this entire thing (extremely hard)

package com.filespark.files;

import java.io.File;
import java.util.*;

import com.filespark.Config;
import com.filespark.windows.DereferenceWindowsShortcut;

public class ScanWindowsRecent {

    private ScanWindowsRecent(){}; 
    
    public static List<File> getRecentFiles(int numberOfFilesToFetch){

        String recentFilesPath = System.getenv("APPDATA") + "\\Microsoft\\Windows\\Recent";
        File recentFilesDirectory = new File(recentFilesPath);
        List<File> recentFiles = new ArrayList<>();

        if (!recentFilesDirectory.exists() || !recentFilesDirectory.isDirectory()){

            System.err.println("Folder DNE or not directory: " + recentFilesPath);
            return recentFiles;
            
        }

        File[] files = recentFilesDirectory.listFiles(File::isFile);
        if (files == null) return recentFiles;

        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
        files = Arrays.copyOfRange(files, 0, Math.min(files.length, 25));

        int filesAdded = 0;

        for (File shortcut : files) {

            String fileName = shortcut.getName().toLowerCase();

            if (fileName.endsWith(".lnk")) {

                System.out.println("Resolving: " + shortcut.getName());
                boolean containsValidExtension = Config.allowedExtensions.stream().anyMatch(fileName::contains);

                if (containsValidExtension) {

                    try {

                        String resolvedPath = DereferenceWindowsShortcut.dereferenceByLiteralPath(shortcut.getAbsolutePath());

                        if (resolvedPath != null && !resolvedPath.isBlank()) {

                            System.out.println("Resolved Path: " + resolvedPath);

                            File targetFile = new File(resolvedPath);

                            if (targetFile.exists() && targetFile.isFile()) {

                                String targetFileName = targetFile.getName().toLowerCase();
                                boolean isAllowed = Config.allowedExtensions.stream().anyMatch(targetFileName::endsWith);

                                if (isAllowed) {

                                    System.out.println("Added: " + targetFile.getName());
                                    recentFiles.add(targetFile);
                                    filesAdded++;

                                } 
                                else {

                                    System.err.println("Skipped: " + targetFile.getName());

                                }

                            } 
                            else {

                                System.err.println("Resolved path is not a valid file: " + resolvedPath);

                            }

                        } 
                        else {

                            System.err.println("Could not resolve: " + shortcut.getName());

                        }

                    } catch (Exception e) {
                        System.err.println("Error resolving: " + shortcut.getName());
                    }

                } 
                else {

                    System.err.println("Skipping: " + shortcut.getName() + " (No valid file extension in name)");

                }

            }

            if (filesAdded >= numberOfFilesToFetch) break;

        }

        System.out.println("Returning " + recentFiles.size() + " real files");
        return recentFiles;

    }

}
