package com.xh.activity.thread;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import com.xh.activity.thread.load.LoadDex;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

public class PackerApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        File cache=getCacheDir();
        File dir=new File(cache,"dex");
        if(!dir.exists())
            dir.mkdirs();
        File saveFile=new File(dir,"rel.apk");
        if(!saveFile.exists())
            try {
                DecodeDex.save(getAssets().open("test.apk"),saveFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        new LoadDex(base).load(saveFile.getPath());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Class<Application> cls= (Class<Application>) Class.forName("com.b.testh.MyApplication");
            Application application=cls.newInstance();
            Method attachBaseContext= ContextWrapper.class.getDeclaredMethod("attachBaseContext",Context.class);
            if(!attachBaseContext.isAccessible())
                attachBaseContext.setAccessible(true);
            attachBaseContext.invoke(application,getBaseContext());
            application.onCreate();
            ActivityThread.alterApplication(application,getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
