package com.example.dasser.popular.movies.stage2;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dasser.popular.movies.stage2.database.Contract;
import com.example.dasser.popular.movies.stage2.model.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.dasser.popular.movies.stage2.database.Contract.Main_Movies_COLUMNS;
import static com.example.dasser.popular.movies.stage2.model.Constants.DETAILS_LOADER_ID;


public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private final String TAG = DetailsActivity.class.getSimpleName();
    private LoaderManager.LoaderCallbacks loaderCallback;

    @BindView(R.id.title) TextView title;
    @BindView(R.id.plotSynopsis) TextView plotSynopsis;
    @BindView(R.id.release_date) TextView releaseDate;
    @BindView(R.id.rating_average) TextView ratingAverage;
    @BindView(R.id.poster) ImageView poster;
    @BindView(R.id.favorite_fab) FloatingActionButton favoriteFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        ButterKnife.bind(this);

        loaderCallback = this;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            getSupportLoaderManager().initLoader(DETAILS_LOADER_ID, bundle, this);
        else
            Log.e(TAG, "Error staring Activity");
    }

    public void restartLoader() {
        getSupportLoaderManager().restartLoader(DETAILS_LOADER_ID, null, loaderCallback);
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


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        assert args != null;
        return new CursorLoader(
                this,
                Contract.MoviesEntry.CONTENT_URI,
                Main_Movies_COLUMNS,
                Contract.MoviesEntry.COLUMN_MOVIE_ID + "=?",
                new String[]{String.valueOf(args.getInt(Constants.EXTRA_MOVIE_ID))},
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
        poster.setImageBitmap(Utils.getBitmapFromByteArray(
                data.getBlob(Contract.COLUMN_MOVIE_POSTER)));

        if(data.getInt(Contract.COLUMN_MOVIE_FAV) == 1)
            favoriteFab.setImageResource(R.drawable.ic_star_white_24dp);
        displayFirstViewsAndHideSpan();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        Log.v(TAG, "In onLoaderReset");
    }

    private void displayFirstViewsAndHideSpan() {
        title.setVisibility(View.VISIBLE);
        plotSynopsis.setVisibility(View.VISIBLE);
        releaseDate.setVisibility(View.VISIBLE);
        ratingAverage.setVisibility(View.VISIBLE);
        poster.setVisibility(View.VISIBLE);
        favoriteFab.setVisibility(View.VISIBLE);
    }
}
