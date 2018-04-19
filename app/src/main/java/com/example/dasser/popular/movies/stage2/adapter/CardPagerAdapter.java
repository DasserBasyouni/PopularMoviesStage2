package com.example.dasser.popular.movies.stage2.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dasser.popular.movies.stage2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.dasser.popular.movies.stage2.utils.Utils.VideosSiteType.youTube;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private final String TAG = CardPagerAdapter.class.getSimpleName();

    private List<CardView> mViews;
    private float mBaseElevation;
    private boolean isDataReviews;
    private List<String> authors, contents, keys, names, types;
    private Context context;

    public CardPagerAdapter(Context context, String key, String name, String  type) {
        mViews = new ArrayList<>();
        isDataReviews = false;
        keys = Arrays.asList(key.split(","));
        names = Arrays.asList(name.split(","));
        type = type.replace("YouTube", youTube);
        types = Arrays.asList(type.split(","));
        this.context = context;
        for(int i=0 ; i<keys.size() ;i++)
            mViews.add(null);
    }

    public CardPagerAdapter(Context context, String author, String content) {
        mViews = new ArrayList<>();
        isDataReviews = true;
        authors = Arrays.asList(author.split("__,__"));
        contents = Arrays.asList(content.split("__,__"));
        this.context = context;
        for(int i=0 ; i<authors.size() ;i++)
            mViews.add(null);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        if (isDataReviews)
            return authors.size();
        else
            return keys.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view ;

        if (isDataReviews) {
            view = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.reviews_card_view, container, false);
            container.addView(view);
            bindReviews(authors.get(position), contents.get(position), view);
        }else {
            view = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.trailers_card_view, container, false);
            container.addView(view);
            bindTrailers(keys.get(position), names.get(position), types.get(position), view);
        }
        CardView cardView = view.findViewById(R.id.card_view);
        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bindReviews(String author, String content, View view) {
        ((TextView) view.findViewById(R.id.author)).setText(author);
        ((TextView) view.findViewById(R.id.content)).setText(content);
    }

    private void bindTrailers(final String key, String name, String type, View view) {
        ((TextView) view.findViewById(R.id.name)).setText(name);
        if (type.equals(youTube)) {
            Picasso.get().load(context.getString(R.string.youtube_thumbnail_format, key))
                    .into((ImageView) view.findViewById(R.id.content));

            view.findViewById(R.id.card_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW
                            , Uri.parse(context.getString(R.string.youtube_video_url_format, key))));
                }
            });
        } else
            Log.e(TAG, "bindTrailers - error unknown type: " + type);
    }
}
