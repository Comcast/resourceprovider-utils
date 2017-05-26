package com.xfinity.resourceprovider.sample;

import android.app.Application;
import com.xfinity.resourceprovider.RpApplication;

@RpApplication
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
