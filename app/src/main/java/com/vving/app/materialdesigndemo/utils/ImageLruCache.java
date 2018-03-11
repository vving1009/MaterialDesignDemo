package com.vving.app.materialdesigndemo.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

/**
 * Created by VV on 2017/10/24.
 */

public class ImageLruCache extends LruCache<String, Bitmap> {

    private static final String TAG = "ImageLruCache";

    public ImageLruCache() {
        super(getDefaultLruCacheSize());
        LogUtil.i(TAG, "ImageLruCache: " + getDefaultLruCacheSize());
    }

    public ImageLruCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }

    private static int getDefaultLruCacheSize() {
        return (int) (Runtime.getRuntime().maxMemory()) / 1024 / 8;
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        //return super.sizeOf(key, value);
        return value.getByteCount() / 1024;
    }

    public Bitmap getImage(String key) {
        return get(key);
    }

    public void putImage(String key, Bitmap image) {
        put(key, image);
    }
}
