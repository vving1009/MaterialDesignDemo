package com.vving.app.materialdesigndemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.vving.app.materialdesigndemo.adapter.RecyclerViewAdapter;
import com.vving.app.materialdesigndemo.bean.MovieInfo;
import com.vving.app.materialdesigndemo.presenter.MovieInfoPresenter;
import com.vving.app.materialdesigndemo.utils.LogUtil;
import com.vving.app.materialdesigndemo.view.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements RefreshRecyclerView {

    private static final String TAG = "MainActivity";

    private List<MovieInfo> movieInfos = new ArrayList<>();
    private MovieInfoPresenter moviePresenter;
    private RecyclerViewAdapter recyclerViewAdapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawerlayout)
    DrawerLayout drawerlayout;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolBar();
        initNavigationView();
        initRecyclerView();
        initSwipeRefresh();
        moviePresenter = new MovieInfoPresenter(this);
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_more);
        }
    }

    private void initNavigationView() {
        navigationView.setCheckedItem(R.id.nav_call);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerlayout.closeDrawers();
                return true;
            }
        });
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(movieInfos);
        recyclerView.setAdapter(recyclerViewAdapter);
        //android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void initSwipeRefresh() {
        swipeRefresh.setColorSchemeResources(R.color.colorAccent);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                moviePresenter.getMovieInfo();
                            }
                        });
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // 在onCreate()方法中设置任何标题值都会被重置为AndroidManifest中android:lable的值
        // Toolbar 必须在onCreate()之后设置标题文本，否则默认标签将覆盖我们的设置
        if (toolbar != null) {
            toolbar.setTitle("ToolBar title");
            toolbar.setSubtitle("Subtitle");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.add:
                Toast.makeText(this, "Menu item add.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.remove:
                Toast.makeText(this, "Menu item remove.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about:
                Toast.makeText(this, "Menu item about.", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                Toast.makeText(this, "Show drawer.", Toast.LENGTH_SHORT).show();
                drawerlayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    @OnClick(R.id.fab)
    public void onFloatingActionButtonClicked() {
        Snackbar.make(fab, "Showing content.", Snackbar.LENGTH_SHORT)
                .setAction("Done.", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Snackbar done.", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
        moviePresenter.getMovieInfo();
    }

    @Override
    public void refreshUI(List<MovieInfo> movieInfos) {
        //this.movieInfos = movieInfos;
        /*for (MovieInfo movieInfo : movieInfos) {
            LogUtil.d(TAG, "refreshUI: Movie name=" + movieInfo.getTitle());
            LogUtil.d(TAG, "refreshUI: Movie image=" + movieInfo.getImage());
        }*/

        if (movieInfos != null) {
            this.movieInfos.clear();
            this.movieInfos.addAll(movieInfos);
        }
        recyclerViewAdapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void networkFailed() {
        Toast.makeText(MainActivity.this, "Network connection failed.", Toast.LENGTH_SHORT).show();
        swipeRefresh.setRefreshing(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        moviePresenter.clearDisposable();
    }
}
