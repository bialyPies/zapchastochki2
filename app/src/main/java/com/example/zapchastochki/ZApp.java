package com.example.zapchastochki;

import android.app.Application;

import com.example.zapchastochki.utils.Settings;
import com.example.zapchastochki.utils.Utils;

public class ZApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.settings = new Settings(getApplicationContext());
        Utils.settings.setValue("lastUpdateDate", 0);
        Utils.settings.setValue("lastUpdateTime", 0);
    }
}
