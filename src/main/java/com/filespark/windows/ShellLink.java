package com.filespark.windows;

import com.filespark.Config;

import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinNT;

import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.Pointer;

public class ShellLink {

    private static final Guid.CLSID CLSID_SHELL_LINK =
            new Guid.CLSID(Config.WIN32_CLASS_ID_OBJECT_SHELL_LINK);

    private static final Guid.IID IID_SHELL_LINK_W =
            new Guid.IID(Config.WIN32_INTERFACE_ID_SHELL_LINK_WIDE);

    public static IShellLinkW createShellLink() {

        System.out.println("===== ShellLink.createShellLink() BEGIN =====");
        System.out.println("CLSID_SHELL_LINK = " + CLSID_SHELL_LINK.toGuidString());
        System.out.println("IID_SHELL_LINK_W = " + IID_SHELL_LINK_W.toGuidString());
        System.out.println();

        // 1) Initialize COM
        System.out.println("[1] Calling CoInitialize...");
        WinNT.HRESULT initRes = Ole32.INSTANCE.CoInitialize(null);
        System.out.println("[1] CoInitialize HRESULT = " + (initRes == null ? "NULL" : initRes.intValue()));
        System.out.println();

        // 2) Attempt to create the COM ShellLink object
        PointerByReference ppv = new PointerByReference();
        System.out.println("[2] Calling CoCreateInstance...");
        WinNT.HRESULT hres = Ole32.INSTANCE.CoCreateInstance(
                CLSID_SHELL_LINK,
                null, // no aggregation
                WTypes.CLSCTX_INPROC_SERVER,
                IID_SHELL_LINK_W,
                ppv
        );

        System.out.println("[2] CoCreateInstance HRESULT = " + (hres == null ? "NULL" : hres.intValue()));
        System.out.println("EXTRA TEST, WinNT.S_OK: " + WinNT.S_OK);

        // Dump the error code in hex too
        if (hres != null) {
            System.out.printf("[2] HRESULT (hex) = 0x%08X%n", hres.intValue());
        } else {
            System.out.println("[2] HRESULT (hex) = null");
        }

        Pointer rawPtr = ppv.getValue();
        System.out.println("[2] Returned COM pointer (ppv) = " + rawPtr);
        System.out.println();

        // 3) Failure case
        if (hres == null || hres != WinNT.S_OK || rawPtr == null) {
            System.err.println("!!! ShellLink create FAILED !!!");
            if (hres != null)
                System.err.printf("!!! HRESULT = 0x%08X%n", hres.intValue());
            else
                System.err.println("!!! HRESULT = null");

            System.err.println("!!! ppv = " + rawPtr);
            System.out.println("===== ShellLink.createShellLink() END (FAILED) =====");
            Ole32.INSTANCE.CoUninitialize();
            return null;
        }

        // 4) Success
        System.out.println("[3] ShellLink COM object created successfully!");
        System.out.println("[3] COM pointer = " + rawPtr);
        System.out.println();

        // 5) Do NOT uninitialize yet — let caller manage it
        System.out.println("===== ShellLink.createShellLink() END (SUCCESS) =====");
        return new IShellLinkW(rawPtr);
    }
}
