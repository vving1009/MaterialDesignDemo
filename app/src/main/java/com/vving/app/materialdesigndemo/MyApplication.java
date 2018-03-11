package com.vving.app.materialdesigndemo;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.vving.app.materialdesigndemo.utils.LogUtil;

//import com.antfortune.freeline.FreelineCore;

/**
 * Created by VV on 2017/10/19.
 */

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        //FreelineCore.init(this);
        mContext = getApplicationContext();
        LogUtil.setDebugLevel(LogUtil.VERBOSE);
        LogUtil.setGlobalTag("liwei");
    }

    public static Context getContext() {
        return mContext;
    }

    public int getMemoryCacheSize() {
        // Get memory class of this device, exceeding this amount will throw an
        // OutOfMemory exception.
        final int memClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        LogUtil.d(TAG, "getMemoryCacheSize: memClass="+ memClass);
        // Use 1/8th of the available memory for this memory cache.
        return memClass / 8 * 1024;
    }
}
