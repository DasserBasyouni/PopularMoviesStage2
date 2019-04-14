package com.example.dasser.popular.movies.stage2.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dasser.popular.movies.stage2.R;
import com.github.ybq.android.spinkit.SpinKitView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import static com.example.dasser.popular.movies.stage2.Constants.STRING_SEPARATOR;
import static com.example.dasser.popular.movies.stage2.Constants.VideosSiteType.youTube;


public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder> {

    private final String TAG = TrailersAdapter.class.getSimpleName();
    private final List<String> keys, names, types;
    private final Context context;


    public TrailersAdapter(Context context, String key, String name, String site) {
        keys = Arrays.asList(key.split(STRING_SEPARATOR));
        names = Arrays.asList(name.split(STRING_SEPARATOR));
        site = site.replace("YouTube", youTube);
        types = Arrays.asList(site.split(STRING_SEPARATOR));
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final TextView trailerName;
        private final ImageView thumbnail;
        private final SpinKitView spinKitView;

        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            trailerName = itemView.findViewById(R.id.name);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            spinKitView = itemView.findViewById(R.id.spin_kit);
        }
    }

    @NonNull
    @Override
    public TrailersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.trailers_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final TrailersAdapter.ViewHolder holder, final int position) {
        holder.trailerName.setText(names.get(position));

        if (types.get(position).equals(youTube)) {
            final String key = keys.get(position);

            final String imageUrl = context.getString(R.string.youtube_thumbnail_format, key);
            Picasso.get().load(imageUrl)
                    .into(holder.thumbnail, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.spinKitView.setVisibility(View.GONE);
                            holder.thumbnail.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e(TAG, "Error loading image with URL: " + imageUrl);
                        }
                    });

            holder.cardView.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW
                        , Uri.parse(context.getString(R.string.youtube_video_url_format, key)));

                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                } else
                    Log.e(TAG, "There is no app installed could take you there");
            });
        }
    }

    @Override
    public int getItemCount() {
        return keys.size();
    }

}