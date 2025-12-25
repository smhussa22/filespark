// scans users recently modified files
// @todo: literally this entire thing (extremely hard)

package com.filespark.files;

import java.io.File;
import java.util.*;

import com.filespark.Config;
import com.filespark.os.windows.DereferenceWindowsShortcut;

public class ScanWindowsRecent {

    private ScanWindowsRecent(){}; 
    
    public static List<File> getRecentFiles(int numberOfFilesToFetch){

        String recentFilesPath = System.getenv("APPDATA") + "\\Microsoft\\Windows\\Recent";
        File recentFilesDirectory = new File(recentFilesPath);
        List<File> results = new ArrayList<>();

        if (!recentFilesDirectory.exists() || !recentFilesDirectory.isDirectory()){

            System.err.println("Folder DNE or not directory: " + recentFilesPath);
            return results;
            
        }

        File[] files = recentFilesDirectory.listFiles(File::isFile);
        if (files == null) return results;

        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
        files = Arrays.copyOfRange(files, 0, Config.filesPerFetch);

        for (File shortcut : files) {

            if (!shortcut.getName().toLowerCase().endsWith(".lnk")) continue;

            System.out.println("Resolving: " + shortcut.getName());
            String resolvedPath;

            try {

                resolvedPath = DereferenceWindowsShortcut.dereferenceByLiteralPath(shortcut.getAbsolutePath());

            }
            catch (Exception e) {

                System.err.println("Error resolving: " + shortcut.getAbsolutePath());
                e.printStackTrace();
                continue;

            }

            if (resolvedPath == null || resolvedPath.isBlank()) {

                System.err.println("Could not resolve: " + shortcut.getName());
                continue;

            }

            File target = new File(resolvedPath);

            if (!target.isFile()) {

                System.err.println("Invalid target: " + resolvedPath);
                continue;

            }

            String name = target.getName().toLowerCase();
            boolean allowed = Config.allowedExtensions.stream().anyMatch(name::endsWith);

            if (!allowed) {

                System.err.println("Rejected by extension: " + name);
                continue;

            }

            results.add(target);
            System.out.println("Added: " + target.getName());

            if (results.size() >= numberOfFilesToFetch) {

                break;

            }

        }

        System.out.println("Returning " + results.size() + " real files");
        return results;

    }

}
