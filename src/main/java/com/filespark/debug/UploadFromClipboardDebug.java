package com.filespark.debug;

import com.filespark.os.GlobalHotkeyListener;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;

public class UploadFromClipboardDebug {

    public static void main (String[] args){

        try { // @Todo: make this only work when logged in

            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(new GlobalHotkeyListener());

        } 
        catch (NativeHookException e){ 

            System.err.println(e.getMessage());
            
        }
        
    }
    
}
