package com.filespark.os.windows;

import com.filespark.Config;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.COM.Unknown;

public class IPersistFile extends Unknown {

    // pvInstance -> pointer to a COM object instance
    public IPersistFile (Pointer pvInstance) { super(pvInstance); }

    // LPWSTR -> long ptr to wide string
    // pszFileName -> ptr to wide string; our file name
    // dwMode -> file open mode (0 = r, 1 = w, etc) for our purposes we will pass in 0
    public WinNT.HRESULT Load(WTypes.LPWSTR pszFileName, int dwMode) {

        Object[] args = { this.getPointer(), pszFileName, dwMode};
        return (WinNT.HRESULT) this._invokeNativeObject(Config.WIN32_VTABLE_LOAD, args, WinNT.HRESULT.class);

    }
    
}
