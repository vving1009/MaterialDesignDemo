package com.vving.app.materialdesigndemo.utils;

import android.util.Log;

/**
 * Created by VV on 2017/10/30.
 */

public class LogUtil {
    public static final int VERBOSE = 100;
    public static final int DEBUG = 200;
    public static final int INFO = 300;
    public static final int WARN = 400;
    public static final int ERROR = 500;
    public static final int NOTHING = 600;
    private static String globalTag = null;
    private static int debugLevel = NOTHING;

    public static void setGlobalTag(String tag) {
        globalTag = tag;
    }

    public static void clearGlobalTag() {
        globalTag = null;
    }

    public static void setDebugLevel(int level) {
        debugLevel = level;
    }

    public static void v(String tag, String msg) {
        if (debugLevel <= VERBOSE) {
            if (globalTag == null) {
                Log.v(tag, msg);
            } else {
                Log.v(globalTag, tag + ": " + msg);
            }
        }
    }

    public static void d(String tag, String msg) {
        if (debugLevel <= DEBUG) {
            if (globalTag == null) {
                Log.d(tag, msg);
            } else {
                Log.d(globalTag, tag + ": " + msg);
            }
        }
    }

    public static void i(String tag, String msg) {
        if (debugLevel <= INFO) {
            if (globalTag == null) {
                Log.i(tag, msg);
            } else {
                Log.i(globalTag, tag + ": " + msg);
            }
        }
    }

    public static void w(String tag, String msg) {
        if (debugLevel <= WARN) {
            if (globalTag == null) {
                Log.w(tag, msg);
            } else {
                Log.w(globalTag, tag + ": " + msg);
            }
        }
    }

    public static void e(String tag, String msg) {
        if (debugLevel <= ERROR) {
            if (globalTag == null) {
                Log.e(tag, msg);
            } else {
                Log.e(globalTag, tag + ": " + msg);
            }
        }
    }
}
