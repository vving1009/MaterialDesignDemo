package com.vving.app.materialdesigndemo.presenter;

import android.util.Log;

import com.vving.app.materialdesigndemo.bean.MovieInfo;
import com.vving.app.materialdesigndemo.model.MovieModel;
import com.vving.app.materialdesigndemo.utils.LogUtil;
import com.vving.app.materialdesigndemo.view.RefreshRecyclerView;

import java.util.List;

/**
 * Created by VV on 2017/10/16.
 */

public class MovieInfoPresenter extends BasePresenterImpl implements MoviePresenterListener {

    private static final String TAG = "MoviePresenter";
    private MovieModel movieModel = new MovieModel(this);
    private RefreshRecyclerView RefreshRecyclerView;

    public MovieInfoPresenter(RefreshRecyclerView RefreshRecyclerView) {
        this.RefreshRecyclerView = RefreshRecyclerView;
    }

    public void getMovieInfo() {
        addDisposable(movieModel.getMovieInfo());
    }


    @Override
    public void init() {

    }

    @Override
    public void detachView() {
        RefreshRecyclerView = null;
        super.detachView();
    }

    @Override
    public void onSuccess(List<MovieInfo> movieInfos) {
        LogUtil.d(TAG, "onSuccess: ");
        RefreshRecyclerView.refreshUI(movieInfos);
    }

    @Override
    public void onFailure(List<MovieInfo> movieInfos) {
        LogUtil.d(TAG, "onFailure: ");
        RefreshRecyclerView.networkFailed();
        RefreshRecyclerView.refreshUI(movieInfos);
    }


}
