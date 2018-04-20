package com.example.dasser.popular.movies.stage2;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dasser.popular.movies.stage2.adapter.ReviewsAdapter;
import com.example.dasser.popular.movies.stage2.adapter.TrailersAdapter;
import com.example.dasser.popular.movies.stage2.database.Contract;
import com.example.dasser.popular.movies.stage2.utils.AsyncTasks;
import com.example.dasser.popular.movies.stage2.utils.Utils;
import com.github.ybq.android.spinkit.SpinKitView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.dasser.popular.movies.stage2.Constants.DETAILS_MAIN_DATA_LOADER_ID;
import static com.example.dasser.popular.movies.stage2.Constants.favoriteOrNot.favorite;
import static com.example.dasser.popular.movies.stage2.Constants.favoriteOrNot.notFavorite;
import static com.example.dasser.popular.movies.stage2.database.Contract.Main_Movies_COLUMNS;
import static com.example.dasser.popular.movies.stage2.database.Contract.MoviesEntry.CONTENT_URI;
import static com.example.dasser.popular.movies.stage2.utils.Utils.getRunTimeFormat;
import static com.example.dasser.popular.movies.stage2.utils.Utils.loadPosterImage;


public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private final String TAG = DetailsActivity.class.getSimpleName();

    @BindView(R.id.title) TextView title;
    @BindView(R.id.plotSynopsis) TextView plotSynopsis;
    @BindView(R.id.release_date) TextView releaseDate;
    @BindView(R.id.rating_average) TextView ratingAverage;
    @BindView(R.id.poster) ImageView poster;
    @BindView(R.id.favorite_fab) FloatingActionButton favoriteFab;

    @BindView(R.id.runtime) TextView runtime;
    @BindView(R.id.loading_more_data_tv) TextView loadingMoreData_tv;
    @BindView(R.id.frameLayout1) FrameLayout firstFrameLayout;
    @BindView(R.id.trailers) TextView trailers;
    @BindView(R.id.trailers_rv) RecyclerView trailersRV;
    @BindView(R.id.frameLayout2) FrameLayout secondFrameLayout;
    @BindView(R.id.reviews) TextView reviews;
    @BindView(R.id.reviews_rv) RecyclerView reviewsRV;
    @BindView(R.id.spin_kit) SpinKitView spinKit;
    @BindView(R.id.no_reviews_tv) TextView noReviews_tv;

    private String movie_id;
    private boolean favoriteMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            getSupportLoaderManager().initLoader(DETAILS_MAIN_DATA_LOADER_ID, bundle, this);
        else
            Log.e(TAG, "Error staring Activity");

        setupFavoriteFab();
    }

    private void setupFavoriteFab() {
        favoriteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues favoriteValue = new ContentValues();
                if (favoriteMovie){
                    favoriteFab.setImageResource(R.drawable.ic_favorite_border_red_a700_24dp);
                    favoriteValue.put(Contract.MoviesEntry.COLUMN_MOVIE_FAV, notFavorite);
                }else {
                    favoriteFab.setImageResource(R.drawable.ic_favorite_red_a700_24dp);
                    favoriteValue.put(Contract.MoviesEntry.COLUMN_MOVIE_FAV, favorite);
                }
                int update = getContentResolver()
                        .update(CONTENT_URI, favoriteValue
                                , Contract.MoviesEntry.COLUMN_MOVIE_ID + "=?"
                                , new String[]{movie_id});
                Log.d(TAG, "setupFavoriteFab.onClick - Task Complete " + update + "has been updated");

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void restartLoader() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.EXTRA_MOVIE_ID, Integer.parseInt(movie_id));
        getSupportLoaderManager().restartLoader(DETAILS_MAIN_DATA_LOADER_ID, bundle, this);
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        assert args != null;
        movie_id = String.valueOf(args.getInt(Constants.EXTRA_MOVIE_ID));
        return new CursorLoader(
                this,
                Contract.MoviesEntry.CONTENT_URI,
                Main_Movies_COLUMNS,
                Contract.MoviesEntry.COLUMN_MOVIE_ID + "=?",
                new String[]{movie_id},
                null
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        title.setText(data.getString(Contract.COLUMN_MOVIE_NAME));
        plotSynopsis.setText(data.getString(Contract.COLUMN_MOVIE_SYS));
        releaseDate.setText(data.getString(Contract.COLUMN_MOVIE_YEAR));
        ratingAverage.setText(Utils.getRateFormat(data.getFloat(Contract.COLUMN_MOVIE_RATE)));
        loadPosterImage(TAG, "https://image.tmdb.org/t/p/w185" + data.getString(Contract.COLUMN_MOVIE_POSTER), poster);

        if(data.getInt(Contract.COLUMN_MOVIE_FAV) == favorite){
            favoriteFab.setImageResource(R.drawable.ic_favorite_red_a700_24dp);
            favoriteMovie = true;
        }

        if(data.getString(Contract.COLUMN_MOVIE_RUNTIME) == null) {
            Log.d(TAG, "onLoadFinished - data is null");
            initializeDetailsDataLoader();
        }else{
            Log.d(TAG, "onLoadFinished - data is not null");
            displayDetailsViewsAndHideSpan(
                    setMoreDetailedDataToViewsAndReturnReviewsAvailability(data));
        }
        displayMainViewsAndHideSpan();
    }

    private boolean setMoreDetailedDataToViewsAndReturnReviewsAvailability(Cursor data) {
        runtime.setText(getRunTimeFormat(getBaseContext(), data.getString(Contract.COLUMN_MOVIE_RUNTIME)));

        String author = data.getString(Contract.COLUMN_MOVIE_REVIEWS_A).trim();
        Log.e("Z_1", author);
        boolean areReviewsAvailable = !author.isEmpty();
        Log.e("Z_2", "" + areReviewsAvailable);

        if (areReviewsAvailable){
            ReviewsAdapter reviewsCardAdapter = new ReviewsAdapter(this,
                    data.getString(Contract.COLUMN_MOVIE_REVIEWS_A)
                    , data.getString(Contract.COLUMN_MOVIE_REVIEWS_C));
            setupRecyclerView(reviewsCardAdapter, reviewsRV);
        } else
            noReviews_tv.setVisibility(View.VISIBLE);


        TrailersAdapter trailerCardAdapter = new TrailersAdapter(this,
                data.getString(Contract.COLUMN_MOVIE_TRAILERS_KEYS)
                , data.getString(Contract.COLUMN_MOVIE_TRAILERS_NAMES)
                , data.getString(Contract.COLUMN_MOVIE_TRAILERS_SITES));
        setupRecyclerView(trailerCardAdapter, trailersRV);
        return areReviewsAvailable;
    }

    private void setupRecyclerView(RecyclerView.Adapter adapter, RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this
                , LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void initializeDetailsDataLoader() {
        LoaderManager.LoaderCallbacks<Cursor> moreDataLoaderListener = new LoaderManager.LoaderCallbacks<Cursor>() {

            @NonNull
            @Override
            public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                return AsyncTasks.getGetAndSaveDetailsDataToDatabase(DetailsActivity.this, movie_id);
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
                if(data == null)
                    restartLoader();
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Cursor> loader) {

            }
        };
        getSupportLoaderManager().initLoader(Constants.DETAILS_MORE_DATA_LOADER_ID, null
                , moreDataLoaderListener);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        Log.v(TAG, "In onLoaderReset");
    }

    private void displayMainViewsAndHideSpan() {
        title.setVisibility(View.VISIBLE);
        plotSynopsis.setVisibility(View.VISIBLE);
        releaseDate.setVisibility(View.VISIBLE);
        ratingAverage.setVisibility(View.VISIBLE);
        poster.setVisibility(View.VISIBLE);
        favoriteFab.setVisibility(View.VISIBLE);
    }

    private void displayDetailsViewsAndHideSpan(boolean reviewsAvailable) {
        loadingMoreData_tv.setVisibility(View.GONE);
        spinKit.setVisibility(View.GONE);
        firstFrameLayout.setVisibility(View.VISIBLE);
        secondFrameLayout.setVisibility(View.VISIBLE);
        trailers.setVisibility(View.VISIBLE);
        trailersRV.setVisibility(View.VISIBLE);
        reviews.setVisibility(View.VISIBLE);
        if (reviewsAvailable) {
            reviewsRV.setVisibility(View.VISIBLE);
            noReviews_tv.setVisibility(View.GONE);
        }else{
            reviewsRV.setVisibility(View.GONE);
            noReviews_tv.setVisibility(View.VISIBLE);
        }
        runtime.setVisibility(View.VISIBLE);
    }

}