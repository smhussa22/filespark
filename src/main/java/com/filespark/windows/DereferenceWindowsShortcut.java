package com.filespark.windows;

import com.filespark.Config;
import com.filespark.windows.IPersistFile;
import com.filespark.windows.ShellLink;
import com.filespark.windows.IShellLinkW;

import java.io.File;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.COM.COMUtils;

// pszFile -> preallocated UTF-16 char array where Windows will write the file path
// cchMaxPath -> # of characters inside of the buffer 
// pfd -> a pointer to the WIN32_FIND_DATAW struct containing file metadata 
// flags to control what type of path we want, which we will pass 0 for default behaviour
// pszFileName -> ptr to wide string; our file name
// hres -> the return value for COM; returns 0 on success and !0 on failure

public class DereferenceWindowsShortcut {

    public static String dereferenceByLiteralPath(String literalPath) {

        Ole32.INSTANCE.CoInitialize(null);
        IShellLinkW link = ShellLink.createShellLink();
        char[] pszFile = new char[Config.WIN32_MAX_PATH];
        PointerByReference pfd = new PointerByReference();
        int fFlags = 0;
       
        IPersistFile persistFile = new IPersistFile(link.getPointer());
        WTypes.LPWSTR pszFileName = new WTypes.LPWSTR(literalPath);
        WinNT.HRESULT hres = persistFile.Load(pszFileName, 0);

        if (hres != WinNT.S_OK){

            System.err.println("windows/DereferenceWindowsShortcut.java: failed to get path on hres " + (hres == null ? "null" : hres.intValue()));
            Ole32.INSTANCE.CoUninitialize();
            return null;

        }

        hres = link.GetPath(pszFile, pszFile.length, pfd, fFlags);

        if (hres != WinNT.S_OK){

            System.err.println("windows/DereferenceWindowsShortcut.java: failed to get path on hres " + (hres == null ? "null" : hres.intValue()));
            Ole32.INSTANCE.CoUninitialize();
            return null;

        }

        Ole32.INSTANCE.CoUninitialize();
        return new String(pszFile).trim();

    }


}
