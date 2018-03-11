package com.vving.app.materialdesigndemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vving.app.materialdesigndemo.MovieInfoDetailActivity;
import com.vving.app.materialdesigndemo.R;
import com.vving.app.materialdesigndemo.bean.MovieInfo;
import com.vving.app.materialdesigndemo.utils.LogUtil;
import com.vving.app.materialdesigndemo.view.LoadCardViewImage;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by VV on 2017/10/16.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MovieViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private Context mContext;
    private List<MovieInfo> mList;

    class MovieViewHolder extends RecyclerView.ViewHolder implements LoadCardViewImage {
        CardView cardView;
        ImageView movieImage;
        TextView movieTitle;

        public MovieViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            movieImage = (ImageView) cardView.findViewById(R.id.movie_item_image);
            movieTitle = (TextView) cardView.findViewById(R.id.movie_item_title);
        }

        @Override
        public void refreshUI(File image) {
        }

        @Override
        public void networkFailed() {

        }
    }

    public RecyclerViewAdapter(List<MovieInfo> list) {
        super();
        this.mList = list;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (parent != null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_item, parent, false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final MovieInfo movieInfo;
        if (mList != null) {

            movieInfo = mList.get(position);
            LogUtil.d(TAG, "onBindViewHolder: title=" + movieInfo.getTitle());
            LogUtil.d(TAG, "onBindViewHolder: image=" + movieInfo.getImage());
            holder.movieTitle.setText("[" + getIndex(position) + "] " + movieInfo.getTitle());
            LogUtil.d(TAG, "onBindViewHolder: movieInfo.getImage()=" + movieInfo.getImage());
            Glide.with(mContext).load(movieInfo.getImage()).crossFade(100).into(holder.movieImage);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MovieInfoDetailActivity.class);
                    Bundle data = new Bundle();
                    data.putString(MovieInfoDetailActivity.TITLE, movieInfo.getTitle());
                    data.putString(MovieInfoDetailActivity.IMAGE, movieInfo.getImage());
                    data.putDouble(MovieInfoDetailActivity.RATING, movieInfo.getRating());
                    data.putInt(MovieInfoDetailActivity.YEAR, movieInfo.getReleaseYear());
                    data.putStringArrayList(MovieInfoDetailActivity.GENRE, (ArrayList<String>) movieInfo.getGenre());
                    intent.putExtra(MovieInfoDetailActivity.INTENT_DATA, data);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    private String getIndex(int position) {
        String s = mList.get(position).getImage();
        String[] splitS1 = s.split("/");
        for (String a : splitS1) {
            //LogUtil.d(TAG, "getIndex: a=" + a);
        }
        if (splitS1.length > 5) {
            String[] splitS2 = splitS1[5].split("\\.");
            for (String a : splitS2) {
                //LogUtil.i(TAG, "getIndex: " + a);
            }
            return splitS2[0];
        } else if (splitS1.length == 5) {
            return splitS1[4].substring(1, splitS1[4].length());
        }
        return "";
    }
}
