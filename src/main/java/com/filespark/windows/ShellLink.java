package com.filespark.windows;

import com.filespark.Config;

import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinNT;

import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.Pointer;

public class ShellLink {

    private static final Guid.CLSID CLSID_SHELL_LINK = new Guid.CLSID(Config.WIN32_CLASS_ID_OBJECT_SHELL_LINK);

    private static final Guid.IID IID_SHELL_LINK_W = new Guid.IID(Config.WIN32_INTERFACE_ID_SHELL_LINK_WIDE);

    public static IShellLinkW createShellLink() {

        PointerByReference ppv = new PointerByReference();
        WinNT.HRESULT hres = Ole32.INSTANCE.CoCreateInstance(CLSID_SHELL_LINK, null, WTypes.CLSCTX_INPROC_SERVER, IID_SHELL_LINK_W, ppv);
        Pointer ptr = ppv.getValue();

        if (ptr == null || COMUtils.FAILED(hres)) {

            System.err.printf("CoCreateInstance failed: 0x%08X%n", hres.intValue());
            return null;

        }

        return new IShellLinkW(ptr);

    }

}

