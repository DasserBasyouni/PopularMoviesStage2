package com.example.dasser.popular.movies.stage2.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.dasser.popular.movies.stage2.DetailsActivity;
import com.example.dasser.popular.movies.stage2.model.Constants;
import com.example.dasser.popular.movies.stage2.model.PostersAndIDs;

import java.util.List;

/**
   Created by Dasser on 12-Jun-17.
 */

public class MoviesAdapter extends BaseAdapter {

    private final String TAG = MoviesAdapter.class.getSimpleName();
    private final Context mContext;
    private final List<PostersAndIDs> postersAndIDs;

    public MoviesAdapter(Context c, List<PostersAndIDs> postersAndIDs) {
        mContext = c;
        this.postersAndIDs = postersAndIDs;
    }

    private class ViewHolder {
        private ImageView posterImageView;
    }

    public int getCount() {
        return postersAndIDs.size();
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
        posterImageView.setImageBitmap(postersAndIDs.get(position).getBitmap());

        posterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra(Constants.EXTRA_MOVIE_ID, postersAndIDs.get(position).getID());
                mContext.startActivity(intent);
            }
        });
    }
}
