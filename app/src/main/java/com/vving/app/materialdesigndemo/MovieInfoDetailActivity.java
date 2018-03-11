package com.vving.app.materialdesigndemo;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieInfoDetailActivity extends AppCompatActivity {

    private static final String TAG = "MovieInfoDetailActivity";

    public static final String INTENT_DATA = "intent_data";
    public static final String TITLE = "title";
    public static final String IMAGE = "image";
    public static final String RATING = "rating";
    public static final String YEAR = "year";
    public static final String GENRE = "genre";

    @BindView(R.id.movie_image)
    ImageView movieImage;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.movie_year)
    TextView movieYearText;
    @BindView(R.id.movie_rating)
    TextView movieRatingText;
    @BindView(R.id.movie_genre)
    TextView movieGenreText;
    @BindView(R.id.dumy_content)
    TextView dumyContentText;
    @BindView(R.id.movie_title)
    TextView movieTitleText;

    private String movieTitle;
    private String movieImageUrl;
    private String movieRating;
    private String movieYear;
    private List<String> movieGenre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info_detail);
        ButterKnife.bind(this);

        getMovieInfo();
        initToolbar();
        initMovieInfo();
    }

    private void getMovieInfo() {
        Bundle bundle = getIntent().getBundleExtra(INTENT_DATA);
        if (bundle != null) {
            movieTitle = bundle.getString(TITLE);
            movieImageUrl = bundle.getString(IMAGE);
            movieRating = Double.toString(bundle.getDouble(RATING));
            movieYear = Integer.toString(bundle.getInt(YEAR));
            movieGenre = bundle.getStringArrayList(GENRE);
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbarLayout.setTitle(movieTitle);
        Glide.with(this).load(movieImageUrl)
                .crossFade().into(movieImage);
    }

    private void initMovieInfo() {
        movieTitleText.setText("Title: " + movieTitle);
        movieYearText.setText("Year: " + movieYear);
        movieRatingText.setText("Rating: " + movieRating);
        movieGenreText.setText("Genre: " + movieGenre.toString());
    }

    @OnClick(R.id.fab)
    public void onViewClicked() {
        Snackbar.make(movieYearText, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
