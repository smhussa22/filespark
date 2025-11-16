package com.filespark.windows;

import com.filespark.Config;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;

public class ShellLink {

    private static final Guid.CLSID CLSID_SHELL_LINK = new Guid.CLSID(Config.WIN32_CLASS_ID_OBJECT_SHELL_LINK);
    private static final Guid.IID IID_SHELL_LINK_W = new Guid.IID(Config.WIN32_INTERFACE_ID_SHELL_LINK_WIDE);

    public static IShellLinkW createShellLink() {

        // ppv -> ptr to ptr to com object
        // hres -> the return value for COM; returns 0 on success and !0 on failure
        Ole32.INSTANCE.CoInitialize(null);
        PointerByReference ppv = new PointerByReference();
        WinNT.HRESULT hres = Ole32.INSTANCE.CoCreateInstance(CLSID_SHELL_LINK, null, WTypes.CLSCTX_INPROC_SERVER, IID_SHELL_LINK_W, ppv);

        if (hres != WinNT.S_OK){

            System.err.println("windows/ShellLink.java: failed to get path on hres " + (hres == null ? "null" : hres.intValue()));
            Ole32.INSTANCE.CoUninitialize();
            return null;

        }

        Ole32.INSTANCE.CoUninitialize();
        return new IShellLinkW(ppv.getValue()); // dereference
        
    }
    
}
