package com.example.dasser.popular.movies.stage2;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.example.dasser.popular.movies.stage2.adapter.MoviesAdapter;
import com.example.dasser.popular.movies.stage2.database.Contract;
import com.example.dasser.popular.movies.stage2.model.IdAndPoster;
import com.example.dasser.popular.movies.stage2.utils.AsyncTasks;
import com.example.dasser.popular.movies.stage2.utils.Utils;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.dasser.popular.movies.stage2.utils.Utils.getSortingPreference;
import static com.example.dasser.popular.movies.stage2.utils.Utils.isDataInitialized;
import static com.example.dasser.popular.movies.stage2.utils.Utils.saveSortPreference;
import static com.example.dasser.popular.movies.stage2.database.Contract.Main_Movies_COLUMNS;
import static com.example.dasser.popular.movies.stage2.Constants.MAIN_LOADER_ID;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.grid_view) GridView gridView;
    @BindView(R.id.spin_kit) SpinKitView spin_kit;

    private final String TAG = MainActivity.class.getSimpleName();
    private int mPosition = GridView.INVALID_POSITION;
    private final String SELECTED_KEY = "selected_position";

    private LoaderManager.LoaderCallbacks loaderCallback;


    public void restartLoader() {
        getSupportLoaderManager().restartLoader(MAIN_LOADER_ID, null, loaderCallback);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            savedInstanceState.getBundle("oBundle");
        }
    }

    private boolean allowGettingDataFromWeb(int currentRequestedData) {
        int sortingPreference = getSortingPreference(this, false);
        return sortingPreference != currentRequestedData || !isDataInitialized(this, sortingPreference);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        loaderCallback = this;

        Log.e("Z_url sort", "here is me002 " + getSortingPreference(this, false));
        getSupportLoaderManager().initLoader(MAIN_LOADER_ID, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem item;
        switch (getSortingPreference(this, false)){
            case Utils.DataLoadedMode.sortedByPopularity:
                item = menu.findItem(R.id.action_popular);
                item.setChecked(true);
                break;
            case Utils.DataLoadedMode.sortedByRating:
                item = menu.findItem(R.id.action_rated);
                item.setChecked(true);
                break;
            case Utils.DataLoadedMode.sortedByFavorites:
                item = menu.findItem(R.id.action_favorite);
                item.setChecked(true);
                break;

                default:
                    item = menu.findItem(R.id.action_popular);
                    item.setChecked(true);
                    break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_popular:
                checkMenuItem(item);
                if (allowGettingDataFromWeb(Utils.DataLoadedMode.sortedByPopularity)) {
                    showLoadingProgress();
                    saveSortPreference(this, Utils.DataLoadedMode.sortedByPopularity);
                    restartLoader();
                }
                return true;
            case R.id.action_rated:
                checkMenuItem(item);
                if (allowGettingDataFromWeb(Utils.DataLoadedMode.sortedByRating)) {
                    showLoadingProgress();
                    saveSortPreference(this, Utils.DataLoadedMode.sortedByRating);
                    restartLoader();
                }
                return true;
            case R.id.action_favorite:
                checkMenuItem(item);
                if (allowGettingDataFromWeb(Utils.DataLoadedMode.sortedByFavorites)) {
                    showLoadingProgress();
                    saveSortPreference(this, Utils.DataLoadedMode.sortedByFavorites);
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


    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Log.v(TAG, "In onCreateLoader");

        String cSelection = null;
        String[] cSelectionArg = null;

        int sortingPreference = getSortingPreference(this, false);
        final int dataOnPopularityMode = Utils.DataLoadedMode.sortedByPopularity;
        final int dataOnRatingMode = Utils.DataLoadedMode.sortedByRating;
        final int dataOnFavoriteMode = Utils.DataLoadedMode.sortedByFavorites;

        switch (sortingPreference) {
            case dataOnPopularityMode:
                Log.e("Z_case", "case 1");
                cSelection = Contract.MoviesEntry.COLUMN_MOVIE_RATED + "=?";
                cSelectionArg = new String[]{String.valueOf(dataOnPopularityMode)};
                break;
            case dataOnRatingMode:
                Log.e("Z_case", "case 2");
                cSelection = Contract.MoviesEntry.COLUMN_MOVIE_RATED + "=?";
                cSelectionArg = new String[]{String.valueOf(dataOnRatingMode)};
                break;
            case dataOnFavoriteMode:
                Log.e("Z_case", "case 3");
                cSelection = Contract.MoviesEntry.COLUMN_MOVIE_FAV + "=?";
                cSelectionArg = new String[]{String.valueOf(dataOnFavoriteMode)};
                break;
        }

        CursorLoader cursorLoader = new CursorLoader(
                this,
                Contract.MoviesEntry.CONTENT_URI,
                Main_Movies_COLUMNS,
                cSelection,
                cSelectionArg,
                null
        );

        if (isDataInitialized(this, sortingPreference)) {
            Log.d(TAG, "In onCreateLoader - Data is not null");
            return cursorLoader;
        }else{
            Log.d(TAG, "In onCreateLoader - Data is null");
            return AsyncTasks.getGetAndSaveMainDataToDatabase(this, cursorLoader);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            Log.v(TAG, "In onLoadFinished");

            data.moveToFirst();
            List<IdAndPoster> postersAndIDs = new ArrayList<>();
            do{
                postersAndIDs.add(new IdAndPoster(data.getInt(Contract.COLUMN_MOVIE_ID),
                        data.getString(Contract.COLUMN_MOVIE_POSTER)));
            } while (data.moveToNext());

            gridView.setAdapter(new MoviesAdapter(MainActivity.this, postersAndIDs));
            spin_kit.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        Log.v(TAG, "In onLoaderReset");
    }

}