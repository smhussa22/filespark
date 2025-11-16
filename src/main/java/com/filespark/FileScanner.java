// scans users recently modified files

package com.filespark;

import java.io.File;
import java.util.*;

import com.filespark.windows.DereferenceWindowsShortcut;

public class FileScanner {

    private FileScanner(){};
    
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
        
        for (File shortcut : files){

            String fileName = shortcut.getName().toLowerCase();

            if (fileName.endsWith(".lnk")) {

                System.out.println("Resolving: " + shortcut.getName());

                String resolvedPath = DereferenceWindowsShortcut.dereferenceByLiteralPath(shortcut.getAbsolutePath());
                if (resolvedPath != null && !resolvedPath.isBlank()){

                    File targetFile = new File(resolvedPath);
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

                    System.err.println("Could not resolve: " + shortcut.getName());

                }

            }

            if (filesAdded >= numberOfFilesToFetch) break;

        }

        System.out.println("Returning " + recentFiles.size() + " real files");
        return recentFiles;

    }
}
