package com.xh.activity.thread;

import android.app.Application;
import android.app.Instrumentation;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class ActivityThread {
    private static final Class activityThreadClass;
    private static final Object activityThread;
    private static Application app;
    private static Handler h;

    static {
        Log.e("ActivityThread","classinit");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                Method forName = Class.class.getDeclaredMethod("forName", String.class);
                Method getDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);
                Class<?> vmRuntimeClass = (Class<?>) forName.invoke(null, "dalvik.system.VMRuntime");
                Method getRuntime = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "getRuntime", null);
                Method setHiddenApiExemptions = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "setHiddenApiExemptions", new Class[]{String[].class});
                Object sVmRuntime = getRuntime.invoke(null);
                setHiddenApiExemptions.invoke(sVmRuntime, new Object[]{new String[]{"L"}});
            } catch (Throwable e) {
                Log.e("[error]", "reflect bootstrap failed:", e);
            }
        }
        activityThreadClass = name2class("android.app.ActivityThread");
        if (activityThreadClass == null) {
            activityThread = null;
        } else {
            Object obj = getField(null, getField(activityThreadClass, "sCurrentActivityThread"));
            if (obj == null)
                obj = invokeMethod(null, getMethod(activityThreadClass, "currentActivityThread"));
            activityThread = obj;
            Field insField = getField(activityThreadClass, "mInstrumentation");
            if (insField != null) {
                Instrumentation ins = (Instrumentation) getField(activityThread, insField);
                setField(activityThread, insField, new MyInstrumentation(ins));
            }
            h= (Handler) getField(activityThread,getField(activityThreadClass,"mH"));
            setField(h, getField(Handler.class, "mCallback"), new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    Log.e("handleMessage","what "+msg.what);
                    return false;
                }
            });
            h.sendEmptyMessageDelayed(111,3000);
        }

    }



    public static void alterApplication(Application application, String packageName) {
        app = application;
        setField(activityThread, getField(activityThreadClass, "mInitialApplication"), application);
        Map<String, WeakReference<Object>> mPackages = (Map<String, WeakReference<Object>>) getField(activityThread, getField(activityThreadClass, "mPackages"));
        if (mPackages != null) {
            WeakReference weakReference = mPackages.get(packageName);
            if (weakReference != null) {
                Object o = weakReference.get();
                if (o != null)
                    changeLoadedApkApplication(o, application);
            }
        }
        Map<String, WeakReference<Object>> mResourcePackages = (Map<String, WeakReference<Object>>) getField(activityThread, getField(activityThreadClass, "mResourcePackages"));
        if (mResourcePackages != null) {

            WeakReference weakReference = mPackages.get(packageName);
            if (weakReference != null) {
                Object o = weakReference.get();
                if (o != null)
                    changeLoadedApkApplication(o, application);
            }
        }
    }

    private static void changeLoadedApkApplication(Object loadedApk, Application application) {
        setField(loadedApk, getField(loadedApk.getClass(), "mApplication"), application);
    }

    public static Application getApplication() {
        if (app != null)
            return app;
        app = (Application) getField(activityThread, getField(activityThreadClass, "mInitialApplication"));
        if (app == null)
            app = (Application) invokeMethod(null, getMethod(activityThreadClass, "currentApplication"));
        if (app == null)
            app = (Application) invokeMethod(activityThread, getMethod(activityThreadClass, "getApplication"));
        return app;
    }


    private static Class name2class(String name) {
        try {
            return Class.forName(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Field getField(Class cls, String field) {
        try {
            Field f = cls.getDeclaredField(field);
            if (!f.isAccessible())
                f.setAccessible(true);
            return f;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Object getField(Object obj, Field field) {
        try {
            return field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void setField(Object obj, Field field, Object value) {
        try {
            field.set(obj, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Method getMethod(Class cls, String name, Class... params) {
        try {
            Method method = cls.getDeclaredMethod(name, params);
            if (!method.isAccessible())
                method.setAccessible(true);
            return method;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Object invokeMethod(Object obj, Method method, Object... params) {
        try {
            return method.invoke(obj, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
