// scans users recently modified files

package com.filespark;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.File;
import java.util.*;
import mslinks.ShellLink;

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

        int count = 0;
        
        for (File shortcut : files){

            boolean isLnk = shortcut.getName().toLowerCase().endsWith((".lnk"));
            if (isLnk) {

                try {

                    ShellLink link = new ShellLink(shortcut);
                    String actualPath = link.getLinkInfo().getLocalBasePath();

                    if(actualPath != null && !actualPath.isBlank()){

                        File actualFile = new File(actualPath);
                        String actualFileName = actualFile.getName().toLowerCase();
                        boolean allowedExtension = Config.allowedExtensions.stream().anyMatch(actualFileName::endsWith);
                        
                        if(allowedExtension){

                            System.out.println("Added: " + actualFile.getName());
                            recentFiles.add(actualFile);
                            count++;

                        }

                    }
                    else {

                        System.err.println("Couldn't add: " + shortcut.getName());

                    }

                }
                catch (Exception exception){

                    System.err.println("FileScanner exception:" + exception.getMessage() + " on: " + shortcut.getName());

                }

            }

            if (count >= numberOfFilesToFetch) break;

        }

        System.out.println("Returning " + recentFiles.size() + " real files");
        return recentFiles;

    }
}
