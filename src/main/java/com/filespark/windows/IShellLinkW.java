package com.filespark.windows;

import com.filespark.Config;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinDef;

public class IShellLinkW extends Unknown {

    // pvInstance -> pointer to a COM object instance
    public IShellLinkW (Pointer pvInstance) { super(pvInstance); }

    // pszFile -> preallocated UTF-16 char array where Windows will write the file path
    // cchMaxPath -> # of characters inside of the buffer 
    // pfd -> a pointer to the WIN32_FIND_DATAW struct containing file metadata 
    // flags to control what type of path we want, which we will pass 0 for default behaviour
    public WinNT.HRESULT GetPath (char[] pszFile, int cchMaxPath, PointerByReference pfd, int fFlags){

        Object[] args = { this.getPointer(), pszFile, cchMaxPath, pfd, fFlags};
        return (WinNT.HRESULT) this._invokeNativeObject(Config.WIN32_VTABLE_GETPATH, args, WinNT.HRESULT.class);

    }

    // hwnd -> handle window for UI, we will pass null 
    // flags to control what type of path we want, which we will pass 0 for default behaviour
    public WinNT.HRESULT Resolve(WinDef.HWND hwnd, int fFlags){

        Object[] args = { this.getPointer(), hwnd, fFlags };
        return (WinNT.HRESULT) this._invokeNativeObject(Config.WIN32_VTABLE_RESOLVE, args, WinNT.HRESULT.class);

    }

}
