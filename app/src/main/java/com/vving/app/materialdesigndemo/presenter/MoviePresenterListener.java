package com.vving.app.materialdesigndemo.presenter;

import com.vving.app.materialdesigndemo.bean.MovieInfo;

import java.util.List;

/**
 * Created by VV on 2017/10/16.
 */

public interface MoviePresenterListener {
    void onSuccess(List<MovieInfo> movieInfos);

    void onFailure(List<MovieInfo> movieInfos);
}
