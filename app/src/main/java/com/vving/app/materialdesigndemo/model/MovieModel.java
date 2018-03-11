package com.vving.app.materialdesigndemo.model;

import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vving.app.materialdesigndemo.MyApplication;
import com.vving.app.materialdesigndemo.R;
import com.vving.app.materialdesigndemo.api.RetrofitFactory;
import com.vving.app.materialdesigndemo.bean.MovieInfo;
import com.vving.app.materialdesigndemo.presenter.MoviePresenterListener;
import com.vving.app.materialdesigndemo.utils.FileUtil;
import com.vving.app.materialdesigndemo.utils.LogUtil;

import java.io.File;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by VV on 2017/10/16.
 */

public class MovieModel {
    private static final String TAG = "MovieModel";

    private MoviePresenterListener moviePresenterListener;

    public MovieModel(MoviePresenterListener moviePresenterListener) {
        this.moviePresenterListener = moviePresenterListener;
    }

    public DisposableObserver getMovieInfo() {
        DisposableObserver<Bitmap> disposableObserver = new DisposableObserver<Bitmap>() {
            @Override
            public void onNext(@NonNull Bitmap bitmap) {
                /*LogUtil.d(TAG, "MovieModel: onNext: "+Thread.currentThread());
                Random random = new Random();
                int count = 3;
                while (count-- != 0) {
                    int index1 = random.nextInt(movieInfos.size());
                    int index2 = random.nextInt(movieInfos.size());
                    LogUtil.i(TAG, "MovieModel: rearrangeList: index1=" + index1 + ",index2=" + index2);
                    movieInfos.add(index1, movieInfos.remove(index2));
                }
                moviePresenterListener.onSuccess(movieInfos);*/
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                /*LogUtil.d(TAG, "MovieModel: onError: "+Thread.currentThread());
                StringBuilder sb = new StringBuilder();
                String line;
                try {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(MyApplication.getContext().getResources()
                                    .openRawResource(R.raw.movie)));
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                LogUtil.d(TAG, "MovieModel: onError: " + sb);
                Gson gson = new Gson();
                List<MovieInfo> movieInfos = gson.fromJson(sb.toString(),
                        new TypeToken<List<MovieInfo>>(){}.getType());
                moviePresenterListener.onFailure(movieInfos);*/
            }

            @Override
            public void onComplete() {

            }
        };

        RetrofitFactory.getInstance().getMovieInfoService()
                .getMovieData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Function<Throwable, List<MovieInfo>>() {
                    @Override
                    public List<MovieInfo> apply(@NonNull Throwable throwable) throws Exception {
                        LogUtil.d(TAG, "MovieModel: onErrorReturn: " + Thread.currentThread());
                        /*StringBuilder sb = new StringBuilder();
                        String line;
                        try {
                            BufferedReader br = new BufferedReader(
                                    new InputStreamReader(MyApplication.getContext().getResources()
                                            .openRawResource(R.raw.movie)));
                            while ((line = br.readLine()) != null) {
                                sb.append(line);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        LogUtil.d(TAG, "MovieModel: onError: " + sb);
                        Gson gson = new Gson();
                        List<MovieInfo> movieInfos = gson.fromJson(sb.toString(),
                                new TypeToken<List<MovieInfo>>() {
                                }.getType());*/

                        Gson gson = new Gson();
                        List<MovieInfo> movieInfos = gson.fromJson(FileUtil.loadJsonRawResource(R.raw.movie),
                                new TypeToken<List<MovieInfo>>(){}.getType());
                        moviePresenterListener.onFailure(movieInfos);
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<List<MovieInfo>>() {
                    @Override
                    public void accept(List<MovieInfo> movieInfos) throws Exception {
                        LogUtil.d(TAG, "MovieModel: doOnNext");
                        Random random = new Random();
                        int count = 3;
                        while (count-- != 0) {
                            int index1 = random.nextInt(movieInfos.size());
                            int index2 = random.nextInt(movieInfos.size());
                            LogUtil.i(TAG, "MovieModel: rearrangeList: index1=" + index1 + ",index2=" + index2);
                            movieInfos.add(index1, movieInfos.remove(index2));
                        }
                        moviePresenterListener.onSuccess(movieInfos);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<List<MovieInfo>, ObservableSource<Bitmap>>() {
                    @Override
                    public ObservableSource<Bitmap> apply(@NonNull final List<MovieInfo> movieInfos) throws Exception {
                        LogUtil.d(TAG, "MovieModel: flatMap: " + Thread.currentThread());
                        return Observable.create(new ObservableOnSubscribe<Bitmap>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<Bitmap> observableEmitter) throws Exception {
                                LogUtil.d(TAG, "MovieModel: flatMap: Observable.create");
                                for (MovieInfo movieInfo : movieInfos) {
                                    Bitmap bitmap = Glide.with(MyApplication.getContext())
                                            .load(movieInfo.getImage())
                                            .asBitmap()
                                            .centerCrop()
                                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                            .get();
                                    String[] strings = movieInfo.getImage().split("/");
                                    LogUtil.d(TAG, "MovieModel:" + strings[5]);
                                    FileUtil.saveImageToFile(bitmap, FileUtil.PATH + File.separator + "image_cache", strings[5]);
                                }
                                FileUtil.copyFileDirectory(FileUtil.PATH + File.separator + "image_cache",
                                        FileUtil.PATH + File.separator + "image_cache_copy", "", true);
                            }
                        });
                    }
                })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        //addDisposable(disposable);
                    }
                })
                /*.doOnNext(new Consumer<List<MovieInfo>>() {
                    @Override
                    public void accept(List<MovieInfo> movieInfos) throws Exception {

                    }
                })*/
                .observeOn(AndroidSchedulers.mainThread(), false, 100)
                .subscribe(disposableObserver);
        //.subscribe(subscriber)//在1.x中此方法返回Subscription，而在2.x中是没有返回值的
        //所以增加subscribeWith()方法，用来返回一个Disposable对象
        //使得用户可以CompositeDisposable.add()方法添加对象。1.x为CompositeSubscription
        //其他subscribe()重载方法返回Disposable
        return disposableObserver;
    }

}
