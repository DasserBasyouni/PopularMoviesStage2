package com.example.dasser.popular.movies.stage2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.example.dasser.popularmoviesstage1.adapter.MoviesAdapter;
import com.example.dasser.popularmoviesstage1.retrofit.MoviesAPI;
import com.github.ybq.android.spinkit.SpinKitView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.grid_view)
    GridView gridView;
    @BindView(R.id.spin_kit) SpinKitView spin_kit;

    private final String TAG = MainActivity.class.getSimpleName();
    private final String popular = "popular";

    private boolean allowGetDataFromWeb(int currentRequestedData) {
        return lastDataState != currentRequestedData;
    }

    private class Data {
        final static int stillLoading = 0;
        final static int sortedByPopularity = 1;
        final static int sortedByRating = 2;
    }

    private int lastDataState = Data.stillLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupRetrofit(popular);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.action_popular);
        item.setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_popular:
                checkMenuItem(item);
                if (allowGetDataFromWeb(Data.sortedByPopularity)) {
                    showLoadingProgress();
                    setupRetrofit(popular);
                }
                return true;
            case R.id.action_rated:
                checkMenuItem(item);
                if (allowGetDataFromWeb(Data.sortedByRating)) {
                    showLoadingProgress();
                    setupRetrofit("top_rated");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkMenuItem(MenuItem item) {
        if (item.isChecked()) item.setChecked(false);
        else item.setChecked(true);
    }

    private void showLoadingProgress() {
        spin_kit.setVisibility(View.VISIBLE);
        gridView.setVisibility(View.GONE);
    }

    private void setupRetrofit(final String sortBy) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/movie/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MoviesAPI moviesAPI = retrofit.create(MoviesAPI.class);
        Call<MoviesAPI.ResultModel> connection = moviesAPI.getAllMovies(sortBy);
        connection.enqueue(new Callback<MoviesAPI.ResultModel>() {
            @Override
            public void onResponse(@NonNull Call<MoviesAPI.ResultModel> call, @NonNull Response<MoviesAPI.ResultModel> response) {
                MoviesAPI.ResultModel body = response.body();

                if (body != null) {
                    if (sortBy.equals(popular)) lastDataState = Data.sortedByPopularity;
                    else lastDataState = Data.sortedByRating;

                    spin_kit.setVisibility(View.GONE);
                    gridView.setVisibility(View.VISIBLE);
                    gridView.setAdapter(new MoviesAdapter(MainActivity.this, body.getResults()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesAPI.ResultModel> call, @NonNull Throwable t) {
                Log.e(TAG, "connection.enqueue.onFailure: " + t);
            }
        });
    }
}
