package com.vving.app.materialdesigndemo.api;

import android.graphics.Movie;

import com.vving.app.materialdesigndemo.bean.MovieInfo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by VV on 2017/10/16.
 */

public interface MovieInfoService {

    @GET("json/movies.json")
    Observable<List<MovieInfo>> getMovieData();
}
