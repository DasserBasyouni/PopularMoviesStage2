package com.example.dasser.popular.movies.stage2;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.example.dasser.popular.movies.stage2.adapter.MoviesAdapter;
import com.example.dasser.popular.movies.stage2.database.Contract;
import com.example.dasser.popular.movies.stage2.model.IdAndPoster;
import com.example.dasser.popular.movies.stage2.utils.AsyncTasks;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.dasser.popular.movies.stage2.Constants.DataLoadedMode.sortedByFavorites;
import static com.example.dasser.popular.movies.stage2.Constants.DataLoadedMode.sortedByPopularity;
import static com.example.dasser.popular.movies.stage2.Constants.DataLoadedMode.sortedByRating;
import static com.example.dasser.popular.movies.stage2.Constants.MAIN_LOADER_ID;
import static com.example.dasser.popular.movies.stage2.database.Contract.COLUMN_MOVIE_ID;
import static com.example.dasser.popular.movies.stage2.database.Contract.Main_Movies_COLUMNS;
import static com.example.dasser.popular.movies.stage2.utils.Utils.getSortingPreference;
import static com.example.dasser.popular.movies.stage2.utils.Utils.isDataInitialized;
import static com.example.dasser.popular.movies.stage2.utils.Utils.saveSortPreference;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.grid_view) GridView gridView;
    @BindView(R.id.spin_kit) SpinKitView spin_kit;

    private final String TAG = MainActivity.class.getSimpleName();
    private LoaderManager.LoaderCallbacks loaderCallback;
    private MenuItem popularMenuItem;

    private void restartLoader() {
        getSupportLoaderManager().restartLoader(MAIN_LOADER_ID, null, loaderCallback);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        loaderCallback = this;

        getSupportLoaderManager().initLoader(MAIN_LOADER_ID, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        switch (getSortingPreference(this)){
            case sortedByPopularity:
                popularMenuItem = menu.findItem(R.id.action_popular).setChecked(true);
                break;
            case sortedByRating:
                menu.findItem(R.id.action_rated).setChecked(true);
                break;
            case sortedByFavorites:
                menu.findItem(R.id.action_favorite).setChecked(true);
                break;

            default:
                menu.findItem(R.id.action_popular).setChecked(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_popular:
                checkMenuItem(item);
                if (allowGettingDataFromWeb(sortedByPopularity)) {
                    showLoadingProgress();
                    saveSortPreference(this, sortedByPopularity);
                    restartLoader();
                }
                return true;
            case R.id.action_rated:
                checkMenuItem(item);
                if (allowGettingDataFromWeb(sortedByRating)) {
                    showLoadingProgress();
                    saveSortPreference(this, sortedByRating);
                    restartLoader();
                }
                return true;
            case R.id.action_favorite:
                checkMenuItem(item);
                if (allowGettingDataFromWeb(sortedByFavorites)) {
                    showLoadingProgress();
                    saveSortPreference(this, sortedByFavorites);
                    restartLoader();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean allowGettingDataFromWeb(int currentRequestedData) {
        int sortingPreference = getSortingPreference(this);
        return sortingPreference != currentRequestedData || !isDataInitialized(this, sortingPreference);
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

        int sortingPreference = getSortingPreference(this);
        final int dataOnPopularityMode = sortedByPopularity;
        final int dataOnRatingMode = sortedByRating;
        final int dataOnFavoriteMode = sortedByFavorites;

        switch (sortingPreference) {
            case dataOnPopularityMode:
                cSelection = Contract.MoviesEntry.COLUMN_MOVIE_RATED + "=?";
                cSelectionArg = new String[]{String.valueOf(dataOnPopularityMode)};
                break;
            case dataOnRatingMode:
                cSelection = Contract.MoviesEntry.COLUMN_MOVIE_RATED + "=?";
                cSelectionArg = new String[]{String.valueOf(dataOnRatingMode)};
                break;
            case dataOnFavoriteMode:
                cSelection = Contract.MoviesEntry.COLUMN_MOVIE_FAV + "=?";
                cSelectionArg = new String[]{String.valueOf(dataOnFavoriteMode)};
                break;

            default:
                throw new IllegalArgumentException("Check MainActivity > onCreateLoader() > switch()");
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
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
            Log.v(TAG, "In onLoadFinished");

        cursor.moveToFirst();
        if (cursor.getCount()==0 && getSortingPreference(this) == sortedByFavorites) {
            saveSortPreference(this, sortedByPopularity);
            if (popularMenuItem != null) {
                popularMenuItem.setChecked(true);
                restartLoader();
            }
            Toast.makeText(this, "You have No favorites now, welcome back to Popular Movies list"
                    , Toast.LENGTH_LONG).show();
        } else {
            List<IdAndPoster> postersAndIDs = new ArrayList<>();
            if(cursor.getCount() != 0) {
                do {
                    postersAndIDs.add(new IdAndPoster(cursor.getInt(COLUMN_MOVIE_ID),
                            cursor.getString(Contract.COLUMN_MOVIE_POSTER)));
                } while (cursor.moveToNext());
            }
            gridView.setAdapter(new MoviesAdapter(MainActivity.this, postersAndIDs));
            spin_kit.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        Log.v(TAG, "In onLoaderReset");
    }

}