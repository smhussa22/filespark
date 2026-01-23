package com.filespark.client;

import com.filespark.AppState;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public final class AppStateManager {
    
    private AppStateManager(){}
    private static final ObjectProperty<AppState> state = new SimpleObjectProperty<>(AppState.LOGGED_OUT);

    public static AppState get() { 

        return state.get();

    }

    public static void set (AppState newState) { 

        Platform.runLater(() -> state.set(newState));

    }

    public static ReadOnlyObjectProperty<AppState> property() {

        return state;

    }

}
