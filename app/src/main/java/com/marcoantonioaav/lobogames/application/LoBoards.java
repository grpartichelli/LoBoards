package com.marcoantonioaav.lobogames.application;

import android.app.Application;
import android.content.Context;

public class LoBoards extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        LoBoards.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return LoBoards.context;
    }
}