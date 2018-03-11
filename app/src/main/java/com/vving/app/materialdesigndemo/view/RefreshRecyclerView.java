package com.vving.app.materialdesigndemo.view;

import com.vving.app.materialdesigndemo.bean.MovieInfo;

import java.util.List;

/**
 * Created by VV on 2017/10/16.
 */

public interface RefreshRecyclerView {
    void refreshUI(List<MovieInfo> movieInfos);

    void networkFailed();
}
