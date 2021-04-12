package com.xh.activity.thread;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppComponentFactory;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@TargetApi(Build.VERSION_CODES.P)
public class AppComponent extends AppComponentFactory {
    private AppComponentFactory appComponent;
    AppComponent(AppComponentFactory appComponent){
        this.appComponent=appComponent;
    }
    @TargetApi(Build.VERSION_CODES.Q)
    @NonNull
    @Override
    public ClassLoader instantiateClassLoader(@NonNull ClassLoader cl, @NonNull ApplicationInfo aInfo) {
        return appComponent.instantiateClassLoader(cl, aInfo);
    }

    @NonNull
    @Override
    public Application instantiateApplication(@NonNull ClassLoader cl, @NonNull String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return appComponent.instantiateApplication(cl, className);
    }

    @NonNull
    @Override
    public Activity instantiateActivity(@NonNull ClassLoader cl, @NonNull String className, @Nullable Intent intent) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return appComponent.instantiateActivity(cl, className, intent);
    }

    @NonNull
    @Override
    public BroadcastReceiver instantiateReceiver(@NonNull ClassLoader cl, @NonNull String className, @Nullable Intent intent) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return appComponent.instantiateReceiver(cl, className, intent);
    }

    @NonNull
    @Override
    public Service instantiateService(@NonNull ClassLoader cl, @NonNull String className, @Nullable Intent intent) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return appComponent.instantiateService(cl, className, intent);
    }

    @NonNull
    @Override
    public ContentProvider instantiateProvider(@NonNull ClassLoader cl, @NonNull String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return appComponent.instantiateProvider(cl, className);
    }
}
