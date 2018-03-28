package com.example.dasser.popular.movies.stage2;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dasser.popularmoviesstage1.model.Movie;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailsActivity extends AppCompatActivity {

    private final String TAG = DetailsActivity.class.getSimpleName();

    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.plotSynopsis_tv)
    TextView plotSynopsis_tv;
    @BindView(R.id.releaseDate_tv)
    TextView releaseDate_tv;
    @BindView(R.id.userRate_tv)
    TextView userRate_tv;
    @BindView(R.id.thumbnail_iv)
    ImageView thumbnail_iv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            displayMovieDetails((Movie) bundle.getParcelable("movie"),
                    (Bitmap) bundle.getParcelable("bitmap"));
        else
            Log.e(TAG, "Error staring Activity");
    }

    private void displayMovieDetails(Movie movie, Bitmap bitmap) {
        title_tv.setText(movie.getOriginalTitle());
        plotSynopsis_tv.setText(movie.getOverview());
        releaseDate_tv.setText(Utils.getDateFormat(movie.getReleaseDate()));
        userRate_tv.setText(String.valueOf(Utils.getRateFormat(movie.getVoteAverage())));
        thumbnail_iv.setImageBitmap(bitmap);

        displayViewsAndHideSpan();
    }

    private void displayViewsAndHideSpan() {
        title_tv.setVisibility(View.VISIBLE);
        plotSynopsis_tv.setVisibility(View.VISIBLE);
        releaseDate_tv.setVisibility(View.VISIBLE);
        userRate_tv.setVisibility(View.VISIBLE);
        thumbnail_iv.setVisibility(View.VISIBLE);
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
}
