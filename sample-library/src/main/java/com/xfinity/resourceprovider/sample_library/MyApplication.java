package com.xfinity.resourceprovider.sample_library;

import android.app.Application;

public class MyApplication extends Application {
    private ResourceProvider resourceProvider;

    @Override
    public void onCreate() {
        super.onCreate();
        resourceProvider = new ResourceProvider(this);
    }

    public ResourceProvider getResourceProvider() {
        return resourceProvider;
    }
}
