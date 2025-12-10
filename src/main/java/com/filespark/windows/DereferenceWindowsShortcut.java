package com.filespark.windows;

import com.filespark.Config;
import com.filespark.windows.IPersistFile;
import com.filespark.windows.ShellLink;
import com.filespark.windows.IShellLinkW;

import java.io.File;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.ptr.PointerByReference;

import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.Ole32;

// pszFile -> preallocated UTF-16 char array where Windows will write the file path
// cchMaxPath -> # of characters inside of the buffer 
// pfd -> a pointer to the WIN32_FIND_DATAW struct containing file metadata 
// flags to control what type of path we want, which we will pass 0 for default behaviour
// pszFileName -> ptr to wide string; our file name
// hres -> the return value for COM; returns 0 on success and !0 on failure

public class DereferenceWindowsShortcut {

    public static String dereferenceByLiteralPath(String literalPath) {

        IShellLinkW link = ShellLink.createShellLink();
        if (link == null) return null;

        char[] pszFile = new char[Config.WIN32_MAX_PATH];
        PointerByReference pfd = new PointerByReference();
        int fFlags = 0;
       
        IPersistFile persistFile = new IPersistFile(link.getPointer());
        WTypes.LPWSTR pszFileName = new WTypes.LPWSTR(literalPath);
        WinNT.HRESULT hres = persistFile.Load(pszFileName, 0);

        if (COMUtils.FAILED(hres)) return null;

        hres = link.Resolve(null, 0);
        if (COMUtils.FAILED(hres)) return null;

        hres = link.GetPath(pszFile, pszFile.length, pfd, fFlags);

        if (COMUtils.FAILED(hres)) return null;

        int len = 0;
        while (len < pszFile.length && pszFile[len] != 0) {

            len++;

        }
        return new String(pszFile, 0, len);


    }


}
