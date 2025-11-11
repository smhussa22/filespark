package com.filespark;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.COM.util.Factory;
import com.sun.jna.platform.win32.COM.util.IUnknown;
import com.sun.jna.platform.win32.COM.util.annotation.ComObject;
import com.sun.jna.platform.win32.COM.util.annotation.ComInterface;

import java.io.File;

public class DereferenceWindowsShortcut {

    // using JNA to replicate WIN32 C headers relevant to files under the COM
    // pszFile -> preallocated UTF-16 char array where Windows will write the file path
    // cchMaxPath -> # of characters inside of the buffer 
    // pfd -> a pointer to the WIN32_FIND_DATAW struct containing file metadata 
    // flags to control what type of path we want, which wie will pass 0 for default behaviour
    // hwnd -> handle window for UI, we will pass null 
    // HRESULT will return 0 (false -> no error) or non-zero (true -> error)
    // https://learn.microsoft.com/en-us/windows/win32/shell/links
    // https://learn.microsoft.com/en-us/windows/win32/api/shobjidl_core/nn-shobjidl_core-ishelllinkw
    // https://learn.microsoft.com/en-us/windows/win32/api/objidl/nn-objidl-ipersistfile

    @ComInterface(iid = Config.WIN32_INTERFACE_ID_SHELL_LINK_WIDE)
    public interface IShellLinkW extends IUnknown {

        WinNT.HRESULT GetPath(char[] pszFile, int cchMaxPath, PointerByReference pfd, int fFlags);
        WinNT.HRESULT Resolve(WinDef.HWND hwnd, int fFlags);

    }

    @ComInterface(iid = Config.WIN32_INTERFACE_ID_PERSIST_FILE)
    public interface IPersistFile extends IUnknown{

        WinNT.HRESULT Load(WTypes.LPWSTR pszFileName, int dwMode);

    }

    @ComObject(clsId = Config.WIN32_CLASS_ID_OBJECT_SHELL_LINK)
    public interface ShellLink extends IShellLinkW {}

    public static String dereferenceByLiteralPath(String literalPath){

        String dereferencedPath = null;

        try {

            File shortcut = new File(literalPath);
            if (!shortcut.exists() || shortcut.getName().toLowerCase().isEmpty() ||!shortcut.getName().toLowerCase().endsWith(".lnk")){

                System.err.println("Shortcut not file, DNE, or not lnk file");
                return null;

            }
            Factory factory = new Factory();
            IShellLinkW link = factory.createObject(ShellLink.class);
            IPersistFile persistFile = link.queryInterface(IPersistFile.class);

            WinNT.HRESULT load = persistFile.Load(new WTypes.LPWSTR(shortcut.getAbsolutePath()), 0);
            if (load == null || load.intValue() != 0){

                System.err.println("Failed to load .lnk file on HRESULT " + (load == null ? "null" : load.intValue()));
                factory.disposeAll();
                return null;

            }

            char[] buffer = new char[Config.WIN32_MAX_PATH];
            WinNT.HRESULT hres = link.GetPath(buffer, Config.WIN32_MAX_PATH, null, 0);
            if (hres == null || hres.intValue() != 0){

                System.err.println("Failed to get path on HRESULT " + (hres == null ? "null" : hres.intValue()));
                return null;

            }
            dereferencedPath = Native.toString(buffer);

            factory.disposeAll();

        }
        catch(Exception exception){

            System.err.println("Error in dereferencing GUID: " + exception.getMessage());
            return null;
            
        }

        return dereferencedPath;

    }
    
}
