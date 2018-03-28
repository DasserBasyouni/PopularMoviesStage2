package com.example.dasser.popular.movies.stage2.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.dasser.popularmoviesstage1.DetailsActivity;
import com.example.dasser.popularmoviesstage1.model.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
   Created by Dasser on 12-Jun-17.
 */

public class MoviesAdapter extends BaseAdapter {

    private final String TAG = MoviesAdapter.class.getSimpleName();
    private final Context mContext;
    private final List<Movie> movies;

    public MoviesAdapter(Context c, List<Movie> movies) {
        mContext = c;
        this.movies = movies;
    }

    private class ViewHolder {
        private ImageView posterImageView;
    }

    public int getCount() {
        return movies.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            viewHolder.posterImageView = new ImageView(mContext);
            convertView = viewHolder.posterImageView;
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        initializePosterImageView(viewHolder.posterImageView, position);
        return convertView;
    }

    private void initializePosterImageView(final ImageView posterImageView, final int position) {
        posterImageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT
                , GridView.LayoutParams.MATCH_PARENT));
        posterImageView.setAdjustViewBounds(true);

        Picasso.get().load("https://image.tmdb.org/t/p/w185" + movies.get(position)
                .getPosterPath()).into(posterImageView, new Callback() {
            @Override
            public void onSuccess() {
                posterImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, DetailsActivity.class);
                        intent.putExtra("movie", movies.get(position));
                        intent.putExtra("bitmap", ((BitmapDrawable)
                                posterImageView.getDrawable()).getBitmap());
                        mContext.startActivity(intent);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "error: " + e);
            }
        });
    }
}
