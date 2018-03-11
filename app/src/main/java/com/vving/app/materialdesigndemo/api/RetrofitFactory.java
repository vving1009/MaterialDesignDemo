package com.vving.app.materialdesigndemo.api;

import android.content.Intent;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.vving.app.materialdesigndemo.MyApplication;
import com.vving.app.materialdesigndemo.bean.MovieInfo;
import com.vving.app.materialdesigndemo.utils.LogUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by VV on 2017/10/16.
 */

public class RetrofitFactory {

    private static final long TIMEOUT = 30000;
    private static final String TAG = "RetrofitFactory";
    public static String BASE_URL = "http://api.androidhive.info/";

    private final Retrofit retrofit;
    private final MovieInfoService movieInfoService;
    private static RetrofitFactory retrofitFactory;

    private OkHttpClient getOkHttpClient() {
        //抓log的，要在app的build.gradle的dependencies里面compile 'com.squareup.okhttp3:logging-interceptor:3.4.1'
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); //log的等级，4种等级，这是最详细的一种等级
        File httpCacheDirectory = new File(MyApplication.getContext().getCacheDir(), "responses");//创建缓存文件
        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);//设置缓存10M
        return new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                /*.addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String s) {
                        LogUtil.d(TAG, "RetrofitFactory:OkHttpClient:log:" + s);
                    }
                }).setLevel(HttpLoggingInterceptor.Level.BASIC))*/
                .addInterceptor(loggingInterceptor)
                /*.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        HttpUrl httpUrl = request.url().newBuilder()
                                //这个地方的addQueryParameter是所有接口都附加的两个值，因各家app而异，加到这个地方就省去了，在retrofit里面单独添加的麻烦。
                                .addQueryParameter("v", "1.0.3")
                                .addQueryParameter("client", "1")
                                .build();
                        request = request.newBuilder().url(httpUrl).build();
                        Response response = chain.proceed(request);
                        Log.d("Response Code", response.code() + "");
                        if (response.code() == 401) {//这个地方可以根据返回码做一些事情。通过sendBroadcast发出去。
                            Intent intent = new Intent("Logout");
                            intent.putExtra("badAuth", true);
                            MyApplication.getContext().sendBroadcast(intent);
                        }
                        return response;
                    }
                })*/
                .cache(cache)
                .build();
    }


    private RetrofitFactory() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        movieInfoService = retrofit.create(MovieInfoService.class);
    }

    public static RetrofitFactory getInstance() {
        if (retrofitFactory == null) {
            synchronized (RetrofitFactory.class) {
                if (retrofitFactory == null) {
                    retrofitFactory = new RetrofitFactory();
                }
            }
        }
        return retrofitFactory;
    }

    public MovieInfoService getMovieInfoService() {
        return movieInfoService;
    }
}
