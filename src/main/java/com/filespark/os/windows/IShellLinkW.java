package com.filespark.os.windows;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.WinNT;

public class IShellLinkW extends Unknown {

    public IShellLinkW(Pointer pvInstance) {
        super(pvInstance);
    }

    // vtable index 3
    public WinNT.HRESULT GetPath(
            char[] pszFile,
            int cchMaxPath,
            PointerByReference pfd,
            int fFlags
    ) {
        Object[] args = { getPointer(), pszFile, cchMaxPath, pfd, fFlags };
        return (WinNT.HRESULT) _invokeNativeObject(3, args, WinNT.HRESULT.class);
    }

    public WinNT.HRESULT Resolve(Pointer hwnd, int fFlags) {
        Object[] args = { getPointer(), hwnd, fFlags };
        return (WinNT.HRESULT) _invokeNativeObject(19, args, WinNT.HRESULT.class);
    }
}

