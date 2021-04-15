package com.xh.activity.thread;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.util.Log;

public class PlugContextWrapper extends ContextWrapper {
    private Resources resources;

    public PlugContextWrapper(Context base, Resources resources) {
        super(base);
        this.resources=resources;
    }

    @Override
    public Resources getResources() {
        Log.d("PlugContextWrapper","getResources");
        return resources != null ? resources : super.getResources();
    }
}
