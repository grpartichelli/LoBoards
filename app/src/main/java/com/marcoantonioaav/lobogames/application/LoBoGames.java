package com.marcoantonioaav.lobogames.application;

import android.app.Application;
import android.content.Context;

public class LoBoGames extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        LoBoGames.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return LoBoGames.context;
    }
}