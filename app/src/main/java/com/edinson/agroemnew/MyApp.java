package com.edinson.agroemnew;

import android.app.Application;

import com.github.mikephil.charting.utils.Utils;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this); // Inicializa Utils
    }
}
