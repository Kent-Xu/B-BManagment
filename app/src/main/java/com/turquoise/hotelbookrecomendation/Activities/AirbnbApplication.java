package com.turquoise.hotelbookrecomendation.Activities;

import android.app.Application;

public class AirbnbApplication extends Application {
    private String id  = "";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
