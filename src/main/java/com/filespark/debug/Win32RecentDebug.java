package com.filespark.debug;

import com.filespark.Config;
import com.filespark.files.ScanWindowsRecent;
import com.sun.jna.platform.win32.Ole32;

import java.util.*;
import java.io.File;

public class Win32RecentDebug {

    public static void main (String[] args) {

        Ole32.INSTANCE.CoInitializeEx(null, Ole32.COINIT_APARTMENTTHREADED);
        try{

            List<File> recents = ScanWindowsRecent.getRecentFiles(Config.filesPerFetch);
            for (File f: recents) { System.out.println(f.getAbsolutePath()); }

        }
        finally{

            Ole32.INSTANCE.CoUninitialize();

        }


    }

}




