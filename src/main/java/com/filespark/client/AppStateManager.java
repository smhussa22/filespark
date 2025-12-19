package com.filespark.client;

import com.filespark.AppState;

public final class AppStateManager {
    
    private AppStateManager(){}
    private static AppState state = AppState.LOGGED_OUT;
    private static Runnable onChange;

    public static AppState get() { 

        return state;

    }

    public static void set (AppState newState) { 

        state = newState;
        if (onChange != null) onChange.run();

    }

    public static void setOnChange (Runnable r) {

        onChange = r;

    }

}
