package com.vving.app.materialdesigndemo.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vving.app.materialdesigndemo.MyApplication;
import com.vving.app.materialdesigndemo.R;
import com.vving.app.materialdesigndemo.bean.MovieInfo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.EventListener;
import java.util.List;

/**
 * Created by VV on 2017/10/25.
 */

public class FileUtil {

    private static final String TAG = "FileUtil";
    public static final String PATH = Environment.getExternalStorageDirectory().toString();
    //注意小米手机必须这样获得public绝对路径
    //private File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();
    public static final String FOLDER_NAME = "image_cache";

    public static void logFileDirectory() {
        LogUtil.d(TAG, "FileUtil: logFileDirectory: getExternalStorageState:" + Environment.getExternalStorageState());
        LogUtil.d(TAG, "FileUtil: Environment.DIRECTORY_DCIM:" + Environment.DIRECTORY_DCIM);
        LogUtil.d(TAG, "FileUtil: Environment.DIRECTORY_PICTURES:" + Environment.DIRECTORY_PICTURES);
        LogUtil.d(TAG, "FileUtil: logFileDirectory: getExternalStorageState(file):" + Environment.getExternalStorageState(new File("/storage/emulated/0/DCIM")));
        LogUtil.d(TAG, "FileUtil: logFileDirectory: getDataDirectory:" + Environment.getDataDirectory());
        LogUtil.d(TAG, "FileUtil: logFileDirectory: getExternalStorageDirectory:" + Environment.getExternalStorageDirectory());
        LogUtil.d(TAG, "FileUtil: logFileDirectory: getRootDirectory:" + Environment.getRootDirectory());
    }

    public static File createFolder(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        LogUtil.d(TAG, "creatCacheFile: file path = " + file.getPath());
        return file;
    }

    //搜索目录，存储目录，扩展名，是否进入子文件夹
    public static void copyFileDirectory(@NonNull String srcPath, String desPath, String extension, boolean isIterative) {
        LogUtil.d(TAG, "copyFileDirectory:srcPath=" + srcPath);
        LogUtil.d(TAG, "copyFileDirectory:desPath=" + desPath);
        File root = new File(srcPath);
        File[] files = root.listFiles();
        for (File file : files) {
            if (file.isDirectory() && !file.getName().contains("\\.") && isIterative) {
                copyFileDirectory(file.getPath(), desPath + File.separator + file.getName(), extension, isIterative);
            } else if (file.isFile()) {
                if (file.getName().substring(file.getName().length() - extension.length()).equals(extension)) {
                    copyFile(file, desPath);
                } else if (extension.equals("")) {
                    copyFile(file, desPath);
                }
            }
        }
    }

    public static void copyFile(@NonNull File file, @NonNull String desPath) {
        LogUtil.d(TAG, "copy file=" + file.getAbsolutePath());
        String desFile = desPath + File.separator + file.getName();
        LogUtil.d(TAG, "copy file desPath=" + desPath);
        createFolder(desPath);
        //srcFiles = mContext.getAssets().list(assetsPath);
        FileInputStream fis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            fis = new FileInputStream(file);
            fos = new FileOutputStream(desFile);
            bos = new BufferedOutputStream(fos);
            byte[] buffer = new byte[fis.available()];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                //不用bos.write(buffer);最后一次读取数据的时候可能不够mBufferSize，比如mBufferSize=1024，
                //但是最后只有1个字节，那么剩下1023个字节就是多余的，所以文件就大一些
                bos.write(buffer, 0, len);
            }
            bos.flush();
            bos.close();
            fos.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            LogUtil.d(TAG, (bos == null) + "");
        }
    }

    public static void saveImageToFile(@NonNull Bitmap image, @NonNull String path, String imageName) {
        LogUtil.d(TAG, "saveImageToFile");
        File folder = createFolder(path);
        String fileName;
        if (imageName != null) {
            fileName = imageName;
        } else {
            fileName = System.currentTimeMillis() + ".jpg";
        }
        File imageFile = new File(folder, fileName);

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            fos = new FileOutputStream(imageFile);
            bos = new BufferedOutputStream(fos);
            image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String loadJsonRawResource(int rawResourceId) {
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(MyApplication.getContext().getResources()
                            .openRawResource(rawResourceId), "utf-8"));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        LogUtil.d(TAG, "loadRawResource: " + sb);
        return sb.toString();
    }
}
