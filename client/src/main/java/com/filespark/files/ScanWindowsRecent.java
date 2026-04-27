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

        if (!recentFilesDirectory.exists() || !recentFilesDirectory.isDirectory()) return results;

        File[] files = recentFilesDirectory.listFiles(File::isFile);
        if (files == null) return results;

        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
        files = Arrays.copyOfRange(files, 0, Config.filesPerFetch);

        for (File shortcut : files) {

            if (!shortcut.getName().toLowerCase().endsWith(".lnk")) continue;

            String resolvedPath;

            try {

                resolvedPath = DereferenceWindowsShortcut.dereferenceByLiteralPath(shortcut.getAbsolutePath());

            }
            catch (Exception ignored) {

                continue;

            }

            if (resolvedPath == null || resolvedPath.isBlank()) continue;

            File target = new File(resolvedPath);

            if (!target.isFile()) continue;

            String name = target.getName().toLowerCase();
            boolean allowed = Config.allowedExtensions.stream().anyMatch(name::endsWith);

            if (!allowed) continue;

            results.add(target);

            if (results.size() >= numberOfFilesToFetch) break;

        }

        return results;

    }

}
