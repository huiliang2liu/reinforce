package com.b.testh;

import android.app.Application;
import android.util.Log;


public class MyApplication extends Application {
    private static final String TAG="MyApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"onCreate");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.e(TAG,"onTerminate");
    }
}
